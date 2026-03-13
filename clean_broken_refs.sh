#!/bin/bash

# Script para limpiar TODAS las referencias rotas en Element Chat
# Después de eliminar spaces, polls, analytics, devtools, rageshake, etc.

BASE_DIR="/data/data/com.termux/files/home/element-chat/vector/src/main/java"

echo "🧹 Limpiando referencias rotas en Element Chat..."

# Función para eliminar imports rotos
clean_imports() {
    local file="$1"
    
    # Eliminar imports de analytics
    sed -i '/import im\.vector\.app\.features\.analytics\./d' "$file"
    
    # Eliminar imports de spaces
    sed -i '/import im\.vector\.app\.features\.spaces\./d' "$file"
    
    # Eliminar imports de poll
    sed -i '/import im\.vector\.app\.features\.poll\./d' "$file"
    
    # Eliminar imports de devtools
    sed -i '/import im\.vector\.app\.features\.devtools\./d' "$file"
    
    # Eliminar imports de rageshake
    sed -i '/import im\.vector\.app\.features\.rageshake\./d' "$file"
    
    # Eliminar imports de session
    sed -i '/import im\.vector\.app\.features\.session\./d' "$file"
    
    # Eliminar imports de signout
    sed -i '/import im\.vector\.app\.features\.signout\./d' "$file"
    
    # Eliminar imports de invite
    sed -i '/import im\.vector\.app\.features\.invite\./d' "$file"
    
    # Eliminar imports de labs
    sed -i '/import im\.vector\.app\.features\.labs\./d' "$file"
}

# Exportar la función para usarla con find
export -f clean_imports

# Encontrar todos los archivos .kt y limpiar imports
find "$BASE_DIR" -name "*.kt" -type f -exec bash -c 'clean_imports "$0"' {} \;

echo "✅ Imports rotos eliminados"

# Ahora eliminar archivos completos que ya no sirven
echo "🗑️  Eliminando archivos huérfanos..."

# Eliminar archivos de legs (ya eliminamos el folder pero quedaron archivos sueltos)
find "$BASE_DIR" -path "*/legals/*" -name "*.kt" -type f -delete 2>/dev/null

# Eliminar archivos de ignored users
find "$BASE_DIR" -path "*/settings/ignored/*" -name "*.kt" -type f -delete 2>/dev/null

# Eliminar archivos de account deactivation
find "$BASE_DIR" -path "*/settings/account/deactivation/*" -name "*.kt" -type f -delete 2>/dev/null

# Eliminar archivos de devtools en settings
find "$BASE_DIR" -path "*/settings/devtools/*" -name "*.kt" -type f -delete 2>/dev/null

# Eliminar archivos de crosssigning
find "$BASE_DIR" -path "*/settings/crosssigning/*" -name "*.kt" -type f -delete 2>/dev/null

# Eliminar archivos de push
find "$BASE_DIR" -path "*/settings/push/*" -name "*.kt" -type f -delete 2>/dev/null

# Eliminar archivos de threepids
find "$BASE_DIR" -path "*/settings/threepids/*" -name "*.kt" -type f -delete 2>/dev/null

# Eliminar archivos de troubleshoot
find "$BASE_DIR" -path "*/settings/troubleshoot/*" -name "*.kt" -type f -delete 2>/dev/null

# Eliminar archivos de roomdirectory
find "$BASE_DIR" -path "*/roomdirectory/*" -name "*.kt" -type f -delete 2>/dev/null

# Eliminar archivos de grouplist
find "$BASE_DIR" -path "*/grouplist/*" -name "*.kt" -type f -delete 2>/dev/null

# Eliminar archivos de contactsbook
find "$BASE_DIR" -path "*/contactsbook/*" -name "*.kt" -type f -delete 2>/dev/null

# Eliminar AutoRageShaker (ya no existe BugReporter)
rm -f "$BASE_DIR/im/vector/app/AutoRageShaker.kt" 2>/dev/null

echo "✅ Archivos huérfanos eliminados"

echo "🎉 Limpieza completada!"
