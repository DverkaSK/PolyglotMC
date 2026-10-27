# Releasing PolyglotMC to Maven Central

Coordinates: `sk.dverka:polyglotmc`, from **2.0.1**. Everything up to `2.0.0` exists only
on JitPack (`com.github.DverkaSK:PolyglotMC`).

The Java packages are `ru.dverkask.polyglotmc` and do not follow the group: Central verifies
the group against a namespace you own and never looks at package names.

**Two things are permanent, so read before you start:**

- A version published to Central can **never** be deleted, replaced or re-published.
  A mistake costs a version number.
- A git tag must point at the exact commit that was published. That is why the first
  Central release is 2.0.1, not 2.0.0: the `2.0.0` tag points at a commit without the
  licence and the publishing setup.

---

## One-time setup

The same as for SkinAnatomy, and already done on this machine: the Central Portal account
(namespace `sk.dverka`, verified through a DNS `TXT` record on `dverka.sk`), a user token,
a GPG key published to `keyserver.ubuntu.com` and `keys.openpgp.org`, and these four lines
in `~/.gradle/gradle.properties`:

```properties
signingInMemoryKey=-----BEGIN PGP PRIVATE KEY BLOCK-----\n...one long line...\n
signingInMemoryKeyPassword=<key passphrase>
mavenCentralUsername=<token username>
mavenCentralPassword=<token password>
```

**These are secrets: they go into `~/.gradle/gradle.properties` and nowhere else.**
The step-by-step setup for a new machine is in SkinAnatomy's `RELEASING.md`.

---

## Every release

1. Bump `version` in `build.gradle` and the version in `README.md`. Commit and push.
2. `./gradlew build`
3. `./gradlew publishToMavenCentral`
   The output must show `:signMavenPublication` and end with an uploaded deployment.
   This only **stages** the release (`automaticRelease = false`).
4. Open <https://central.sonatype.com/publishing> and wait for the deployment status:
   - **VALIDATED** → check *Component Files*: `.jar`, `-sources.jar`, `-javadoc.jar`,
     `.pom`, `.module`, each with `.asc`, `.md5`, `.sha1`. Then press **Publish**.
     This is the irreversible step.
   - **FAILED** → read the errors, press **Drop**, fix, upload again. A failed
     deployment publishes nothing, and the same version can be uploaded again.
5. Tag the published commit and push (tags here have no `v` prefix):
   ```bash
   git tag -a X.Y.Z -m "PolyglotMC X.Y.Z"
   git push origin develop X.Y.Z
   ```
6. Wait until the artifact is on Central **and on its Google mirror** - Paper's `libraries:`
   loader reads the mirror, which lags behind:
   ```bash
   P=sk/dverka/polyglotmc/X.Y.Z/polyglotmc-X.Y.Z.pom
   curl -s -o /dev/null -w "central %{http_code}\n" https://repo1.maven.org/maven2/$P
   curl -s -o /dev/null -w "mirror  %{http_code}\n" https://maven-central.storage-download.googleapis.com/maven2/$P
   ```

---

## Pitfalls

1. **Signing switches on only when `signingInMemoryKey` is set.** Plain builds,
   `publishToMavenLocal` and JitPack run without a key; Central rejects an unsigned upload
   by itself.
2. **The GPG key expires** on the date chosen at creation. Extending it is described in
   SkinAnatomy's `RELEASING.md`.
3. **Never move a published tag.** JitPack caches a tag forever by its name, and Central
   cannot replace a version at all - release a new version instead.
