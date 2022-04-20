# shared-costs
Web application to calculate costs for some group of people who spends money together. Note that this is may pet-project to learn Spring

## Deployment
### Docker
1. Build an image using spring-boot gradle plugin task (Docker should be installed & run on your host):
```sh
./gradlew bootBuildImage 
```
2. Deploy application with Docker compose:
```
docker compose up -d
```
3. Stop application server
```
docker compose down
```
