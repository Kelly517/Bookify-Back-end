#!/bin/bash

echo "🧠 Iniciando flujo Git para nueva feature..."

# 1. Pedir nombre de la feature
read -p "📌 Ingresa el nombre de la feature (ej: create-cover-upload): " FEATURE_NAME
BRANCH_NAME="feature/BKF-$FEATURE_NAME"

# 2. Ir a develop y actualizar
echo "🔄 Cambiando a develop y actualizando..."
git checkout develop
git pull origin develop

# 3. Crear nueva rama
echo "🌱 Creando nueva rama: $BRANCH_NAME"
git checkout -b "$BRANCH_NAME"

# 4. Confirmar que ya hiciste tus cambios
read -p "🛠️  ¿Ya hiciste los cambios? (Presiona ENTER para continuar)"

# 5. Hacer commit
echo "📦 Guardando cambios..."
git add .
read -p "📝 Ingresa el mensaje del commit: " COMMIT_MSG
git commit -m "$COMMIT_MSG"

# 6. Subir la feature
echo "☁️ Subiendo la rama al remoto..."
git push origin "$BRANCH_NAME"

# 7. Hacer pull de la feature (por si hubo cambios)
git pull origin "$BRANCH_NAME"

# 8. Merge en develop
echo "🔁 Haciendo merge en develop..."
git checkout develop
git pull origin develop
git merge "$BRANCH_NAME"

# 9. Subir develop actualizado
git push origin develop

# 10. Eliminar rama local y remota
read -p "🧹 ¿Deseas eliminar la rama feature '$BRANCH_NAME'? (y/n): " DELETE_BRANCH
if [[ "$DELETE_BRANCH" == "y" ]]; then
  git branch -d "$BRANCH_NAME"
  git push origin --delete "$BRANCH_NAME"
  echo "✅ Rama eliminada"
else
  echo "📌 Rama conservada"
fi

echo "🎉 ¡Flujo completado exitosamente!"