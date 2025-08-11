# Plataforma de Aulas

## Descrição
Esta é uma aplicação desktop em Java usando Swing para gerenciar e abrir vídeos e PDFs organizados em categorias. Vídeos têm thumbnails gerados com FFmpeg, e PDFs usam ícones do sistema.

## Requisitos
- Java JDK 8 ou superior instalado.
- FFmpeg (incluído no diretório `tools/`, mas certifique-se de que está presente).

## Instalação de Dependências
1. **Instale Java JDK**: Baixe e instale do site oficial da Oracle ou use OpenJDK. Adicione ao PATH se necessário.
2. **FFmpeg**: O executável está em `tools/ffmpeg.exe`. Se não estiver, baixe do site oficial do FFmpeg e extraia `ffmpeg.exe` para `tools/`.

## Como Rodar a Aplicação
1. Navegue até o diretório do projeto: `cd caminho/para/plataforma de teste`.
2. Compile o código: `javac src/PlataformaAulas.java`.
3. Execute: `java -cp src PlataformaAulas`.

Ou em um comando só (PowerShell): `javac src/PlataformaAulas.java; if ($LASTEXITCODE -eq 0) { java -cp src PlataformaAulas }`.

## Estrutura de Pastas
- `videos/`: Contém subpastas para categorias (ex: `VideoAula/` para vídeos, `conteudo/` para PDFs).
- `thumbnails/`: Armazena thumbnails gerados para vídeos.
- `tools/`: Contém `ffmpeg.exe`.

## Uso
- Selecione uma categoria na lista à esquerda.
- Selecione um item na lista central.
- Clique em "Abrir Item" para abrir o vídeo ou PDF.

## Configuração do GitHub
Após clonar ou baixar, para contribuir:
1. Crie um repositório no GitHub.
2. Adicione o remote: `git remote add origin https://github.com/seuusuario/nome-do-repo.git`.
3. Push: `git push -u origin master`.