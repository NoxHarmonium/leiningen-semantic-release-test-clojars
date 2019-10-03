# leiningen-semantic-release-test-clojars

[![CircleCI](https://circleci.com/gh/NoxHarmonium/leiningen-semantic-release-test-clojars.svg?style=svg)](https://circleci.com/gh/NoxHarmonium/leiningen-semantic-release-test-clojars)

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

## Signing

There were a few things that needed to be done to get the gpg signing working on the CircleCI builder with leiningen.

### Exporting the signing key

If you run the following command, the secret key will be dumped to the terminal

```bash
$ gpg -a --export-secret-keys 3D061752D6187B0730E33033902AF4D92DF99864 | cat -e | sed 's/\$/\\n/g'
-----BEGIN PGP PRIVATE KEY BLOCK-----\n
\n
lQPGBF2WZIYBCADdaMf43c6I18CzYFR4cBSNhV9f089WMJwglO98tA0ErVMwjTqz\n
....
hV6eQGcvb7VabZCbAdKb/evQVN7qSsWERQ/3rhCm+Pa+E4v7a4e+1RxqTWm6y1Gq\n
yrycUQ==\n
=K9Rc\n
-----END PGP PRIVATE KEY BLOCK-----\n
```

Copy the entire multiline output.
When you add the environment variable `GPG_KEY` in the CircleCI console
paste the copied text as the value. It will be compressed down to a single
line automatically, no need to do it yourself.

Make sure you run the command in `bash`, I tried it in fish and the `\n`
characters were not printed literally and were not in the copied text.
You need the `\n` characters in the text so the multiline text can be
reconstructed by `echo -e`.

### Disable any interactive GPG features

To avoid the `Error: gpg: signing failed: Inappropriate ioctl for device` error,
you can add these options to `gpg.conf` to prevent any interactive prompts from `gpg`.

```bash
echo "no-tty" >> ~/.gnupg/gpg.conf
echo "batch" >> ~/.gnupg/gpg.conf
```

This will cause these options to be added to every `gpg` command (even the ones run by leiningen).

It is suggested in a lot of places that `GPG_TTY=$(tty)` will fix this issue
but when I tried it I just got a `Required environment variable not set” error`.

### Cache the passphrase

It doesn't seem like we can make leiningen pass the `--passphrase` switch
to gpg at signing time (although maybe I just can't find it).

In the meantime you can do the following to get `gpg-agent` to cache
the passphrase for you before you run `lein deploy`.
That way, when `lein deploy` runs it doesn't need to prompt for it.

First enable `gpg-agent`.

```bash
# Makes gpg use the agent
$ echo use-agent >> ~/.gnupg/gpg.conf
# Make sure that the agent doesn't try to do anything interactive
$ echo pinentry-mode loopback >> ~/.gnupg/gpg.conf
$ echo allow-loopback-pinentry >> ~/.gnupg/gpg-agent.conf
# Make sure the agent is using the latest config.
$ echo RELOADAGENT | gpg-connect-agent
```

Then to force `gpg-agent` to cache the passphrase for a key
we just sign some test data.
It doesn't matter what the test data is, in this case the string "test"
is being signed.
The output is also piped to `/dev/null` because we don't need it.

```bash
$ command: echo "test" | gpg --sign --passphrase "$GPG_PASSPHRASE" -o /dev/null
```

The default expiry for passphrase caching is [10 minutes](https://superuser.com/a/624488/159598)
so if you run this command before a long running build step
it may expire. You should probably run it just before signing time.

### Future work

Things that need to be verified:

- Is there an way to get leiningen to pass the passphrase to gpg?
- Is there a way to get leiningen to pass arguments like `--no-tty` for us?
- Is there a way to cache a passphrase without actually invoking a sign command?