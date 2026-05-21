#!/bin/bash
cd /c/Users/S7ven/Desktop/ws-intelij/blog-api

MAX_ITERATIONS=137
iteration=0

while [ $iteration -lt $MAX_ITERATIONS ]; do
  iteration=$((iteration + 1))
  echo "=== Iteracao $iteration ==="
  
  # Verificar se ha conflitos
  UNMERGED=$(git diff --name-only --diff-filter=U 2>/dev/null)
  
  if [ -z "$UNMERGED" ]; then
    # Sem conflitos, verificar se o rebase esta em progresso
    if [ ! -d .git/rebase-merge ] && [ ! -d .git/rebase-apply ]; then
      echo "Rebase concluido!"
      break
    fi
    # Sem conflitos mas rebase em progresso, continuar
    echo "Sem conflitos, continuando..."
    GIT_EDITOR=true git rebase --continue 2>&1
    if [ $? -ne 0 ]; then
      echo "Erro ao continuar, verificando conflitos..."
    fi
    continue
  fi
  
  echo "Conflitos encontrados:"
  echo "$UNMERGED"
  
  # Resolver conflitos
  echo "$UNMERGED" | while read file; do
    if [ -z "$file" ]; then continue; fi
    echo "Resolvendo: $file"
    
    # Se e arquivo de seed/instantiation/test, remover
    if echo "$file" | grep -qi "instantiation\|seed\|application-test\|application.properties"; then
      echo "  Removendo/usando versao mais recente..."
      if git ls-files --deleted --modified "$file" 2>/dev/null | grep -q .; then
        git rm -f "$file" 2>/dev/null
      else
        git checkout --theirs "$file" 2>/dev/null
        git add "$file"
      fi
    else
      # Para outros arquivos, usar a versao mais recente (theirs = o commit sendo aplicado)
      echo "  Usando versao mais recente..."
      git checkout --theirs "$file" 2>/dev/null
      git add "$file"
    fi
  done
  
  # Adicionar tudo e continuar
  git add -A
  GIT_EDITOR=true git rebase --continue 2>&1
  if [ $? -ne 0 ]; then
    echo "Continuando proxima iteracao..."
  fi
done

echo ""
echo "=== Resultado Final ==="
echo "Total de commits:"
git rev-list --count HEAD
echo ""
echo "Ultimos 20 commits com datas:"
git log --format="%h %ai %s" -20
echo ""
echo "Diff com 7572343 (conteudo original):"
git diff 7572343 --stat HEAD -- src/
