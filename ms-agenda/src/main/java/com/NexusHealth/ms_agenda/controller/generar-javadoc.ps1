# Genera JavaDoc para todos los microservicios (Maven o Directo)

param(
    [string]$javadocPath = "C:\Program Files\Java\jdk-23\bin\javadoc.exe"
)

$microservicios = @(
    "ms-agenda",
    "ms-auditoria", 
    "ms-examenes",
    "ms-gateway",
    "ms-notificaciones",
    "ms-orquestador",
    "ms-pacientes"
)

Write-Host "====================================================" -ForegroundColor Cyan
Write-Host "   NexusHealth - Generador de JavaDoc (Híbrido)" -ForegroundColor Green
Write-Host "====================================================" -ForegroundColor Cyan
Write-Host ""

# Crear carpeta principal
New-Item -ItemType Directory -Path "./docs" -Force | Out-Null

$exitos = 0
$fallos = 0

foreach ($ms in $microservicios) {
    Write-Host "📦 Procesando: $ms" -ForegroundColor Yellow
    
    $msPath = "./$ms"
    $sourcePath = "$msPath/src"
    $outputPath = "./docs/$ms"
    $pomPath = "$msPath/pom.xml"
    
    # Verificar que existe el microservicio
    if (-not (Test-Path $msPath)) {
        Write-Host "  ⚠️  Carpeta $msPath no encontrada" -ForegroundColor Gray
        continue
    }
    
    # Crear carpeta de salida
    New-Item -ItemType Directory -Path $outputPath -Force | Out-Null
    
    # Verificar si tiene pom.xml (proyecto Maven)
    if (Test-Path $pomPath) {
        Write-Host "  📝 Es proyecto Maven" -ForegroundColor Gray
        
        try {
            Push-Location $msPath
            
            # Generar JavaDoc con Maven
            Write-Host "  🔧 Ejecutando: mvn javadoc:javadoc" -ForegroundColor Gray
            mvn javadoc:javadoc -DskipTests -Ddoclint=none -q
            
            if ($LASTEXITCODE -eq 0) {
                # Copiar documentación generada
                if (Test-Path "./target/site/apidocs") {
                    Remove-Item -Path "../$outputPath/*" -Recurse -Force -ErrorAction SilentlyContinue
                    Copy-Item -Path "./target/site/apidocs/*" -Destination "../$outputPath" -Recurse
                    Write-Host "  ✅ $ms - Documentación generada con Maven" -ForegroundColor Green
                    $exitos++
                } else {
                    Write-Host "  ⚠️  No se encontró la documentación generada" -ForegroundColor Yellow
                    $fallos++
                }
            } else {
                Write-Host "  ⚠️  Maven falló, intentando con javadoc directo..." -ForegroundColor Yellow
                # Fallback a javadoc directo
                & $javadocPath -d "../$outputPath" -sourcepath "./src" -subpackages . -author -version -charset UTF-8 2>$null
                
                if ($LASTEXITCODE -eq 0) {
                    Write-Host "  ✅ $ms - Documentación generada (fallback)" -ForegroundColor Green
                    $exitos++
                } else {
                    Write-Host "  ❌ $ms - Error en ambos métodos" -ForegroundColor Red
                    $fallos++
                }
            }
            
            Pop-Location
            
        } catch {
            Write-Host "  ❌ $ms - Error: $_" -ForegroundColor Red
            $fallos++
            Pop-Location -ErrorAction SilentlyContinue
        }
        
    } else {
        # No tiene pom.xml, usar javadoc directo
        Write-Host "  📝 Proyecto sin Maven, usando javadoc directo" -ForegroundColor Gray
        
        # Verificar que hay archivos .java
        $archivosJava = Get-ChildItem -Path $sourcePath -Recurse -Filter "*.java" -ErrorAction SilentlyContinue
        
        if ($archivosJava.Count -eq 0) {
            Write-Host "  ⚠️  No hay archivos .java" -ForegroundColor Gray
            continue
        }
        
        Write-Host "  📁 Archivos .java: $($archivosJava.Count)" -ForegroundColor Gray
        
        try {
            & $javadocPath -d $outputPath `
                           -sourcepath $sourcePath `
                           -subpackages . `
                           -author `
                           -version `
                           -charset UTF-8 `
                           -encoding UTF-8 `
                           -docencoding UTF-8 2>$null
            
            if ($LASTEXITCODE -eq 0) {
                Write-Host "  ✅ $ms - Documentación generada (directo)" -ForegroundColor Green
                $exitos++
            } else {
                Write-Host "  ❌ $ms - Error en javadoc directo" -ForegroundColor Red
                $fallos++
            }
            
        } catch {
            Write-Host "  ❌ $ms - Error: $_" -ForegroundColor Red
            $fallos++
        }
    }
    
    Write-Host ""
}

# Resumen final
Write-Host "====================================================" -ForegroundColor Cyan
Write-Host "📊 RESUMEN FINAL:" -ForegroundColor Green
Write-Host "  ✅ Exitosos: $exitos" -ForegroundColor Green
Write-Host "  ❌ Fallidos: $fallos" -ForegroundColor Red
Write-Host ""
Write-Host "📂 Documentación generada en: ./docs/" -ForegroundColor Cyan
Write-Host ""
Write-Host "📄 Para abrir un microservicio específico:" -ForegroundColor Yellow
Write-Host "   start ./docs/ms-orquestador/index.html" -ForegroundColor Gray
Write-Host "====================================================" -ForegroundColor Cyan