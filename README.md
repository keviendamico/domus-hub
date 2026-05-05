# Domus Hub

A Telegram-based smart home hub for controlling IoT devices over the local network, with a Kafka event pipeline and a Grafana dashboard for usage metrics. Designed to run on a **Raspberry Pi 5**.

Currently supports **Shelly Bulb** smart lights, with the architecture built to accommodate additional IoT devices.

## Architecture

```
Telegram
   ↓ webhook POST /webhook
BotWebhookController
   ↓
DomusHubBotService
   ├── ShellyDeviceInterface (turnOn / turnOff)
   └── LightEventLogProducer → Kafka topic "light_event_log_topic"
                                        ↓
                              LightEventLogConsumer
                                        ↓
                              LightEventLogService
                                        ↓
                                   PostgreSQL
                                        ↓
                                     Grafana
```

## Stack

| Layer | Technology |
|---|---|
| Backend | Java 21, Spring Boot 3.5.0 |
| Messaging | Apache Kafka (KRaft, 1 broker) |
| Bot | Telegram Bot API — webhook mode, telegrambots v9.5.0 |
| Device | Shelly Bulb — local REST API |
| Persistence | PostgreSQL 14 |
| Dashboard | Grafana |
| Container | Docker + Docker Compose |
| Target | Raspberry Pi 5 — `linux/arm64` |

## Bot Usage

Send `/lights` to get an inline keyboard with available rooms. Tap a button to toggle the light.

```
/lights → [Living Room ON]  [Living Room OFF]
          [Kitchen ON]      [Kitchen OFF]
```

Each action calls the Shelly REST API and publishes an event to Kafka, which is then persisted to PostgreSQL for Grafana metrics.

## Services

The project has two Docker Compose files:

- `docker-compose.yml` — infrastructure only (postgres, kafka, kafka-ui, grafana). Used in local development.
- `docker-compose.prod.yml` — adds the `app` service (Spring Boot). Used on the Raspberry Pi.

### Local infrastructure

| Service | Image | Port |
|---|---|---|
| `postgres` | `postgres:14.22` | 5432 |
| `kafka` | `confluentinc/cp-kafka:7.7.8` | 9092 |
| `kafka-ui` | `provectuslabs/kafka-ui:v0.7.2` | 8081 |
| `grafana` | `grafana/grafana` | 3000 |

### Production (Pi)

All of the above plus:

| Service | Port |
|---|---|
| `app` (Spring Boot) | 8080 |

## Getting Started

### Prerequisites

- Docker + Docker Compose
- ngrok (for local webhook tunnel)
- A Shelly Bulb on the local network

### Run locally

1. Start the infrastructure:
```bash
docker compose up -d
```

2. Create the database schema:
```bash
psql -h localhost -U root -d domus_hub -f schema.sql
```

3. Start a tunnel with ngrok:
```bash
ngrok http 8080
```

4. Set the environment variables and run the app with the `local` Spring profile:
```
TELEGRAM_BOT_TOKEN=...
TELEGRAM_WEBHOOK_URL=https://<ngrok-url>
DEVICE_LIGHT_LIVING_ROOM=http://192.168.1.x
```

### Deploy on Raspberry Pi

1. Create a `.env` file in the project root (never commit this file):
```
POSTGRES_USER=
POSTGRES_PASS=

TELEGRAM_BOT_TOKEN=
TELEGRAM_WEBHOOK_URL=

DEVICE_LIGHT_LIVING_ROOM=
```

2. Set up a Cloudflare Tunnel pointing to `localhost:8080` and use the resulting URL as `TELEGRAM_WEBHOOK_URL`.

3. Start everything:
```bash
docker compose -f docker-compose.yml -f docker-compose.prod.yml up -d
```

4. Create the database schema:
```bash
docker exec -i domus-hub-db psql -U root -d domus_hub < schema.sql
```

### Spring profiles

| Profile | Behavior |
|---|---|
| `local` | Uses `MockShellyDevice` — no real bulb needed |
| `production` | Uses `ShellyDevice` — calls the real Shelly REST API |

## Grafana

The Grafana dashboard is auto-provisioned at startup from `./grafana/provisioning`. Access it at `http://localhost:3000`.

Available panels:
- Daily turn-ons (time series)
- Total time on per room in minutes (bar chart)
- Turn-ons vs turn-offs per room (bar chart)
- Sessions by hour of day (bar chart)
- Last turn-on per room (table)

## Project Structure

```
com.domushub
├── config/           # Bot, Kafka, REST client configuration
├── consumer/         # Kafka consumer
├── controller/       # Webhook REST controller
├── device/           # Shelly device abstraction and implementations
├── model/            # JPA entities and Kafka message records
├── producer/         # Kafka producer
├── repository/       # Spring Data JPA repositories
└── service/          # Bot logic, event persistence
```