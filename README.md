# NEXO — Your AI. Your Companion.

Etapa 1: esqueleto (tema, navegación, 3 pantallas base).

## Compilar con GitHub Actions (gratis)
1. Crea un repositorio en GitHub y sube todo este contenido a la rama `main`.
2. Pestaña **Actions** → workflow "Android CI".
3. Al terminar, descarga el artefacto `nexo-debug-apk`, descomprímelo e instala el `.apk`.
4. Si falla, abre el job, copia el error (o descarga `reportes-de-error`) y pégalo en el chat.

## Local (opcional)
Android Studio → Open → carpeta del proyecto → Run.

## Probar Gemini (Etapa de prueba)
1. Crea una clave gratis en https://aistudio.google.com/app/apikey
2. En NEXO: Configuración → pega la clave → **Guardar y verificar**.
3. Debe aparecer **API conectada**. Si no, la pantalla muestra el motivo real.
4. Home → **Chat de prueba**.

La clave se cifra con Android Keystore. El modelo por defecto es editable
(si Google lo renombra, cámbialo en Configuración).

## Rediseño de interfaz
Barra inferior (Inicio · Personajes · Ajustes). La mascota es provisional y vectorial;
en Inicio puedes cambiar su estado con los botones. Personajes guarda la selección.
Ajustes → Proveedor de IA es la pantalla de conexión con Gemini.
Los ajustes marcados "Próximamente" aún no hacen nada.
