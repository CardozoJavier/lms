# Makefile

# Default target (help message)
.PHONY: help
help:
	@echo "Available commands:"
	@echo "	make up		- Run the full stack (app + database)"
	@echo "	make dev	- Run only the database (development mode)"
	@echo "	make down	- Stop and remove all containers"
	@echo "	make logs	- Show container logs"
	@echo "	make build	- Rebuild all containers"

# Production: Run both app and database
.PHONY: up
up:
	docker compose -f docker-compose.yml up

# Development: Run only database
.PHONY: dev
dev:
	docker compose -f docker-compose.yml -f docker-compose.dev.yml up

# Stop all containers
.PHONY: down
down:
	docker compose down

# Show logs
.PHONY: logs
logs:
	docker compose logs -f

# Build containers
.PHONY: build
build:
	docker compose build