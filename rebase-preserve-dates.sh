#!/bin/bash
cd /c/Users/S7ven/Desktop/ws-intelij/blog-api

# Voltar para main
git checkout main

# Script editor que modifica o rebase-todo
# DROP: commits desnecessários
# FIXUP: README consecutivos e security fixes
cat > /tmp/rebase-editor-preserve.sh << 'EDITOR_SCRIPT'
#!/bin/bash
FILE="$1"

# Commits para DROP
DROP=(
  "d47a082"  # feat: add database seed config for test profile
  "f4e715f"  # feat: seed test data with Post entities in Instantiation
  "00663b1"  # feat: update Instantiation to use AuthorDTO and new Post constructor
  "f8bcbc4"  # chore(seed): update Instantiation data for new Comment structure with author
  "9610fa9"  # fix(config): persist alex after linking post00 in Instantiation
  "36d0a89"  # chore(seed): update Instantiation for new User fields and comment-post relation
  "54d8bfd"  # chore: remove verbose mongodb debug logging from test config
  "ff36028"  # chore: remove debug logging and clean up test imports
  "a24f1f6"  # chore/remove-instantiation-seed
  "a755881"  # chore: recover uncommitted changes
  "4303c88"  # chore: remove blank line in Post entity
  "f3bad4b"  # refactor(post): reorder controller and service methods (no functional change)
  "be513e2"  # chore: remove unused imports and dead code
  "6a6636c"  # refactor(user): remove unused findAll endpoint and reorder methods
  "7d45a43"  # test/should-create-post (duplicado mais antigo)
  "52dfe98"  # fix/adjust-details (duplicado mais antigo)
)

# Commits para FIXUP (squash no anterior, preservando data do anterior)
FIXUP=(
  "84f5631"   # docs: update README -> squash no anterior
  "ae5c7f2"   # docs: replace TODO -> squash no anterior
  "1962f25"   # docs: remove roadmap -> squash no anterior
  "cb7ed2d"   # docs: add Flyway -> squash no anterior
  "374f2d8"   # fix: security vulnerabilities -> squash no anterior
  "7572343"   # docs: update README -> squash no anterior
)

while IFS= read -r line; do
  hash=$(echo "$line" | awk '{print $2}')
  
  is_drop=false
  for d in "${DROP[@]}"; do
    if [ "$hash" = "$d" ]; then
      is_drop=true
      break
    fi
  done
  
  is_fixup=false
  for f in "${FIXUP[@]}"; do
    if [ "$hash" = "$f" ]; then
      is_fixup=true
      break
    fi
  done
  
  if [ "$is_drop" = true ]; then
    echo "drop $hash"
  elif [ "$is_fixup" = true ]; then
    echo "fixup $hash"
  else
    echo "$line"
  fi
done < "$FILE" > "$FILE.tmp"
mv "$FILE.tmp" "$FILE"
EDITOR_SCRIPT
chmod +x /tmp/rebase-editor-preserve.sh

echo "=== Executando rebase preservando datas originais ==="
export GIT_SEQUENCE_EDITOR=/tmp/rebase-editor-preserve.sh
git rebase -i --root --reapply-cherry-picks

echo ""
echo "=== Resultado ==="
echo "Total de commits apos rebase:"
git rev-list --count HEAD
echo ""
echo "Ultimos 20 commits:"
git log --oneline -20
echo ""
echo "Verificando datas:"
git log --format="%h %ai %s" -20
