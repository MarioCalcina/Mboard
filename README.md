# Mboard

Teclado para Android, privado y 100 % local.

## Características

- **Sin internet**: la app no tiene permisos de red; todo funciona en el teléfono.
- **Sin copia en la nube**: las palabras aprendidas y el historial no se suben a la copia de seguridad de Google (el respaldo manual a archivo sigue disponible en los ajustes).
- **Sin recolección de datos**: la función de recolección de datos de gestos está desactivada.
- Diccionarios, temas y distribuciones personalizables, escritura multilingüe, portapapeles, modo a una mano, teclado dividido y teclado numérico.

ID de paquete: `com.mboard.keyboard`.

### Escritura por deslizamiento (gestos)

La escritura por deslizamiento necesita una librería externa de Google (cerrada) que se carga manualmente desde *Ajustes → Avanzado → Cargar la biblioteca de escritura gestual*. La librería funciona dentro de Mboard; al no tener la app permiso de internet, tampoco ella puede conectarse a la red.

### Distribuciones personalizadas

El formato de las distribuciones de teclado está descrito en [layouts.md](layouts.md).

## Instalar

Descarga el APK de la última versión en la pestaña [*Releases*](https://github.com/MarioCalcina/Mboard/releases). Las versiones de *Releases* se firman siempre con la misma clave, así que se pueden actualizar sin desinstalar (y sin perder palabras aprendidas ni ajustes).

> El APK *debug* de *Actions* se firma con una clave distinta en cada compilación: sirve para probar, pero para actualizarlo hay que desinstalar la versión anterior. Antes de hacerlo, guarda una copia desde *Ajustes → Avanzado → Copia de seguridad y restauración*.

## Compilar

- **GitHub Actions**: pestaña *Actions* → *Build debug APK* → *Run workflow*; el APK aparece en *Artifacts*.
- **Android Studio**: abrir la carpeta y ejecutar `./gradlew assembleDebug` (o `assembleRelease`, ver abajo).
- **Pruebas**: `./gradlew testRunTestsUnitTest` (se ejecutan también en *Actions* con cada push a `main`).

### Publicar una versión

Solo hay que configurarlo una vez:

1. Crea una clave de firma y guárdala en un lugar seguro (si la pierdes, no podrás publicar actualizaciones compatibles):
   ```
   keytool -genkeypair -v -keystore mboard.jks -keyalg RSA -keysize 4096 -validity 10000 -alias mboard
   ```
2. En GitHub: *Settings → Secrets and variables → Actions → New repository secret*, crea:
   - `MBOARD_KEYSTORE_BASE64`: el contenido de `mboard.jks` en base64 (`base64 -w0 mboard.jks` en Linux/Git Bash).
   - `MBOARD_KEYSTORE_PASSWORD`: la contraseña del keystore.
   - `MBOARD_KEY_ALIAS`: `mboard` (o el alias que usaste).
   - `MBOARD_KEY_PASSWORD`: la contraseña de la clave.

Después, cada versión se publica subiendo una etiqueta; el workflow *Release APK* compila, firma y crea la *Release*:

```
git tag v4.1
git push origin v4.1
```

Para compilar un release firmado en local, define las mismas variables de entorno (`MBOARD_KEYSTORE_FILE` con la ruta al `.jks`, más `MBOARD_KEYSTORE_PASSWORD`, `MBOARD_KEY_ALIAS` y `MBOARD_KEY_PASSWORD`) y ejecuta `./gradlew assembleRelease`. Nunca subas el archivo `.jks` ni las contraseñas al repositorio.

## Licencia

Mboard es software libre bajo la licencia **GNU GPL v3.0** (ver [LICENSE](LICENSE)). Si distribuyes el APK, debes ofrecer también el código fuente.

Mboard es una versión modificada de software libre publicado bajo GPL v3, derivada del [teclado de AOSP](https://android.googlesource.com/platform/packages/inputmethods/LatinIME/) (Apache 2.0, ver [LICENSE-Apache-2.0](LICENSE-Apache-2.0)). Los avisos de copyright y licencia de cada archivo se conservan en el código fuente.
