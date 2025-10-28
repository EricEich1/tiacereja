#!/bin/bash

# Lista de arquivos de teste para corrigir
FILES=(
    "src/test/java/com/example/festas/controller/TipoEventoControllerTest.java"
    "src/test/java/com/example/festas/controller/ClienteControllerTest.java"
    "src/test/java/com/example/festas/controller/EnderecoControllerTest.java"
    "src/test/java/com/example/festas/controller/SolicitacaoOrcamentoControllerTest.java"
)

for file in "${FILES[@]}"; do
    echo "Processing $file..."
    
    # Adicionar imports e mocks se necessário
    if ! grep -q "import com.example.festas.repository.UsuarioRepository;" "$file"; then
        sed -i.tmp '/import com.example.festas.service.*;/a\
import com.example.festas.repository.UsuarioRepository;
import com.example.festas.security.ITokenService;
import org.springframework.security.test.context.support.WithMockUser;
' "$file"
    fi
    
    # Adicionar @MockBean para security
    if ! grep -q "@MockBean.*ITokenService" "$file"; then
        sed -i.tmp '/@MockBean/,/@Test/ {
            /@MockBean.*Service.*;/a\
\
    @MockBean\
    private ITokenService tokenService;\
\
    @MockBean\
    private UsuarioRepository usuarioRepository;
        }' "$file"
    fi
    
    # Adicionar @WithMockUser em todos os @Test
    if ! grep -q "@WithMockUser" "$file"; then
        sed -i.tmp '/^    @Test$/a\
    @WithMockUser
' "$file"
    fi
    
    # Limpar arquivos .tmp
    rm -f "${file}.tmp"
done

echo "Done!"


