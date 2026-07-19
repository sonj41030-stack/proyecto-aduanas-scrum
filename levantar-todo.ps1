$base = "C:\Users\Klare Stella\Downloads\proyecto-aduanas-scrum-main"
Start-Process pwsh -ArgumentList "-NoExit", "-Command", "cd '$base\ms-auth\ms-auth' && .\mvnw.cmd spring-boot:run"
Start-Sleep -Seconds 3
Start-Process pwsh -ArgumentList "-NoExit", "-Command", "cd '$base\ms-tramites' && mvn spring-boot:run"
Start-Sleep -Seconds 3
Start-Process pwsh -ArgumentList "-NoExit", "-Command", "cd '$base\ms-reportes' && mvn spring-boot:run"
Start-Sleep -Seconds 3
Start-Process pwsh -ArgumentList "-NoExit", "-Command", "cd '$base\api-gateway\api-wateway' && .\mvnw.cmd spring-boot:run"
Start-Sleep -Seconds 3
Start-Process pwsh -ArgumentList "-NoExit", "-Command", "cd '$base\ms-sag' && .\mvnw.cmd spring-boot:run"
Start-Sleep -Seconds 3
Start-Process pwsh -ArgumentList "-NoExit", "-Command", "cd '$base\ms-pdi' && .\mvnw.cmd spring-boot:run"
Write-Host "Los 6 microservicios se estan levantando en ventanas separadas."
Write-Host "Espera a ver 'Started ... Application' en cada una antes de probar el frontend."