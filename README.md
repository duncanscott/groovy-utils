# groovy-utils

## Build and test

Install Java 17 and run the Gradle wrapper from the project root:

```sh
./gradlew --no-daemon --no-configuration-cache check assemble
```

Dependencies are downloaded from Maven Central. The HTTP client integration test
also reads a public fixture from `gist.githubusercontent.com`, so tests require
network access. Artifactory credentials are no longer used.

## Jackson 3 compatibility

The `main` branch uses Jackson 3.2.3 through
`tools.jackson.core:jackson-databind`. The `json-util` and `http-client` APIs now
accept and return Jackson 3 tree types such as `tools.jackson.databind.JsonNode`,
`ObjectNode`, and `ArrayNode`. Consumers using these types must migrate their
imports and use Jackson 3; Jackson 2 tree types cannot be passed to these APIs.
Jackson annotations still use the compatible `com.fasterxml.jackson.annotation`
package supplied transitively by databind. See the
[Jackson 3 migration guide](https://github.com/FasterXML/jackson/blob/main/jackson3/MIGRATING_TO_JACKSON_3.md).
The HTTP client depends on Groovy core and XML directly; it does not pull in
the unused Groovy YAML module and its Jackson 2 dependencies.

The release version is `11.0.0-jackson-3x`. Existing date helpers retain their
UTC string format and date-array behavior. Java 17 and Gradle 9.1.0 remain the
build requirements.

## Publish with GitLab CI/CD

The root `.gitlab-ci.yml` runs only for **tag pushes** matching
`^[0-9]+(\.[0-9]+)*(-\S+)?$`, the same rule as `json-message`: a numeric version
with an optional suffix starting with `-`, followed by one or more non-whitespace
characters. Examples include `10`, `10.0`, `10.0.0`, `10.0.0-jackson-2x`, and
`10.0.0-rc.1`; `v10.0.0`, `10.0.0-`, and tags containing spaces are rejected. Branch pushes,
merge requests, schedules, and manually created pipelines do not create release
pipelines. The tag must exactly match `version` in `gradle.properties`.

The pipeline runs all libraries' checks before publishing any package. It then
publishes the following artifacts, using `org.duncanscott` as the Maven group
and the shared version from `gradle.properties`:

- `enum-util`
- `http-client`
- `json-util`
- `on-demand-cache`
- `web-util`

Each publication retains its main JAR, sources JAR, POM, Gradle module metadata,
and the existing additional `gradle.properties` artifact. JARs, publication
metadata, and test reports are also retained as CI job artifacts for 30 days.

Packages are uploaded to this project's GitLab Maven registry:

```text
$CI_API_V4_URL/projects/$CI_PROJECT_ID/packages/maven
```

Gradle authenticates with the automatically supplied `CI_JOB_TOKEN` using the
`Job-Token` header, following the
[GitLab Maven registry documentation](https://docs.gitlab.com/user/packages/maven_repository/).
No manually stored publishing token or Artifactory configuration is needed.

Before pushing the first release tag:

1. Confirm that the package registry is enabled for this GitLab project.
2. Make a **Docker executor** runner with the `docker-build` tag available to the
   project, or update `.gitlab-ci.yml` to use an available Docker runner's tag.
   The tag follows the existing PPS pipeline example; its executor must be
   confirmed. The `eclipse-temurin:17-jdk` image supplies Java 17. This job needs
   neither Docker-in-Docker nor privileged mode.
3. Review the intended release version and working changes, then use the release
   helper below (or commit and push the matching tag manually). Disable any
   existing Artifactory publishing automation when switching releases to GitLab.

## Create a release tag

The `tag.sh` helper follows the `json-message` workflow:

```sh
./tag.sh
```

It checks the current version against local tags and tags on **every configured
remote**, incrementing the final numeric component until it finds an unused
version. Set the full version, including any suffix, in `gradle.properties`.
For example, if `10.0.0-jackson-2x` already exists, the next candidate is
`10.0.1-jackson-2x`; the suffix is preserved unchanged. An unused version is
kept as written. All five libraries use the same full version.
It updates only the `version=` line in `gradle.properties`, stages all
working changes (including untracked files), commits them as `version <version>`,
and creates an annotated tag. If there are no changes, it tags the existing
commit. It then pushes the current branch and **only the new tag** to each remote.
With the current checkout, that includes both GitLab (`origin`) and GitHub (`hub`).
The GitLab tag push triggers the CI pipeline.

Review your working tree before running this helper: it commits all changes.
It requires a checked-out branch and access to every configured remote, and
stops if a Git command fails. Pushes to multiple remotes are sequential, so a
failure may leave earlier remotes updated. It does not run tests locally; CI
runs them before package publication.

`tag.sh` matches `json-message` main commit `c882353`. The Gradle release helper
uses the same suffix format and incrementing behavior, while preserving this
project's shared version across modules and its properties file layout.
The wrapper locates the project directory and falls back to an installed SDKMAN
or macOS JDK if `JAVA_HOME` is invalid. Additional arguments are passed to Gradle.
To inspect the task order without executing release actions:

```sh
./tag.sh --dry-run
```

This displays the task plan; it does not query tags or calculate the next version.

The release tasks (`getGitBranch`, `checkLocalTag`, `checkRemoteTag`,
`updateVersion`, and `tag`) are registered once on the root project by
`gradle/scripts/tag.gradle`.

## Later use by json-message

The current version is `11.0.0-jackson-3x`, so a matching release publishes
`org.duncanscott:enum-util:11.0.0-jackson-3x` along with the other four libraries.
Downstream projects can update their dependency versions after publication.
No changes to `json-message` are part of this migration.

Its dependency repository can use this project's Maven endpoint:

```text
https://code.jgi.doe.gov/api/v4/projects/<groovy-utils-project-id>/packages/maven
```

For another private project's CI job to download these packages, add that
project to this project's **Settings > CI/CD > Job token permissions** allowlist
and ensure the user starting the downstream pipeline has access. A group Maven
endpoint can also be used to consume packages from multiple projects.
