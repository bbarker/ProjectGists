nix-shell -p 'idrisPackages.with-packages (with idrisPackages; [ contrib effects ])' -p closurecompiler
