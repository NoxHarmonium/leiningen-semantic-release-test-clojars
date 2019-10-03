# leiningen-semantic-release-test-clojars

## Summary

A library which exists only to test the [leiningen-semantic-release](https://github.com/NoxHarmonium/leiningen-semantic-release) plugin
for **[semantic-release](https://github.com/semantic-release/semantic-release)**.

The interesting bits are:

- [project.clj](./project.clj) - The project configuration which includes required keys for deployment to [clojars](https://clojars.org/leiningen-semantic-release-test-clojars).
- [.releaserc.json](./.releaserc.json) - The semantic-release configuration file that specified the steps to be followed for release.
- [.circleci/config.yml](./.circleci/config.yml) - The build script that builds and deploys the library automatically using semantic-release.

The code under the src and test directories is not at all interesting.

## Envrionment Variables

The environment variables that are needed for this build to work are:

- GH_TOKEN - A Github personal access token to push updates to Github.
- GPG_KEY - The secret key to import into gpg and sign the artifact with before uploading to clojars.
- GPG_PASSPHRASE - The passphrase to access the key in GPG_KEY.
- LEIN_USERNAME - The username used to upload to clojars.
- LEIN_PASSWORD	- THe password used to upload to clojars.

GPG_KEY was exported with the following command:

```bash
`gpg -a --export-secret-keys <fingerprint> | cat -e | sed 's/\$/\\n/g'`
```