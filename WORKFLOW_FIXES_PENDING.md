# GitHub Actions Workflow Fixes - Correcciones Pendientes

## ✅ CORREGIDOS

### 1. gradle-wrapper-validation.yml - Action Deprecated
**ANTES:**
```yaml
uses: gradle/wrapper-validation-action@f9c9c575b8b21b6485636a91ffecd10e558c62f6 # v3.5.0
```

**DESPUÉS:**
```yaml
uses: gradle/actions/wrapper-validation@v3
```

**ESTADO**: ✅ PENDIENTE DE APLICAR

---

## ⚠️ PENDIENTES

### 2. build.yml - Docker step en job exodus
**PROBLEMA**: `uses: docker://` está DEPRECATED

**SOLUCIÓN**: Cambiar a `run: docker run ...`

**ARCHIVO**: `.github/workflows/build.yml`

---

### 3. build.yml - jq tracker check
**PROBLEMA**: Comparación incorrecta con múltiples valores

**SOLUCIÓN**: Usar `jq -e` con select

**ARCHIVO**: `.github/workflows/build.yml`

---

### 4. strict-ci.yml - Actions desactualizados
**PROBLEMA**: Usa v4 en lugar de v6/v5/v7

**SOLUCIÓN**: Actualizar:
- `actions/checkout@v4` → `@v6`
- `actions/setup-java@v4` → `@v5`
- `actions/upload-artifact@v4` → `@v7`
- `actions/download-artifact@v4` → `@v8`

**ARCHIVO**: `.github/workflows/strict-ci.yml`

---

### 5. strict-ci.yml - JDK inconsistente
**PROBLEMA**: Usa JDK 17 Zulu, debería ser JDK 21 Temurin

**SOLUCIÓN**: Cambiar a:
```yaml
java-version: '21'
distribution: 'temurin'
```

**ARCHIVO**: `.github/workflows/strict-ci.yml`

---

## 📋 ARCHIVOS A MODIFICAR

1. `.github/workflows/gradle-wrapper-validation.yml` - Action deprecated
2. `.github/workflows/build.yml` - Docker y jq fixes
3. `.github/workflows/strict-ci.yml` - Actualizar actions y JDK

---

**NOTA**: Los workflows build.yml y build-detailed.yml ya están correctos con las últimas versiones.
