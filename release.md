To make a release:

- change the current SNAPSHOT version to a non-snapshot one (remove -SNAPSHOT)
- run `mvn clean deploy -Dgpg.passphrase=<..>`

(you need the proper gpg keys for the OSS account setup) 