# CryptoFrypto - Cryptocurrency Algorithmic Trading Platform

A distributed, event-driven cryptocurrency trading system built with Spring Boot and Apache Kafka for real-time market data processing and automated trading strategy execution.

## Features

### Currently Implemented
- Real-time market data ingestion via Coinbase WebSocket API
- Kafka-based event streaming architecture
- Concurrent feed management with configurable thread pools
- Support for multiple cryptocurrency pairs
- Production and sandbox environment configurations

### Planned
- Kafka Streams processing engine for trading strategies
- REST API orchestrator for dynamic configuration
- Trading strategy implementations (Moving Averages, RSI, MACD)
- ML-based strategy optimization

## Getting Started

### Prerequisites
- Java 21+
- Docker (for Kafka)
- Maven 3.9+

### Installation

1. Clone the repository
```bash
git clone https://github.com/StylistMercurial1130/CryptoFrypto.git
cd CryptoFrypto/marketfeed
```

2. Start Kafka using Docker
```bash
docker run -d --name kafka -p 9092:9092 apache/kafka:latest
```

3. Build and run the application
```bash
./mvnw clean install
./mvnw spring-boot:run
```

## Configuration

Key settings in `application.yml`:

```yaml
producer:
  coinbase:
    source:
      marketdata:
        production: wss://ws-feed.exchange.coinbase.com
        sandbox: wss://ws-feed-public.sandbox.exchange.coinbase.com

threadpool:
  configuration:
    corePoolSize: 10
    maxPoolSize: 50
    queueCapacity: 100
```

## Technology Stack

- **Framework**: Spring Boot 3.5.6
- **Messaging**: Apache Kafka, Spring Kafka
- **Language**: Java 21
- **Build Tool**: Maven

## Project Status

🚧 **Active Development** - The market data ingestion service is functional. Stream processing and orchestration layers are currently being developed.

## Disclaimer

This software is for educational purposes only. Cryptocurrency trading involves substantial risk of loss. Use at your own risk.

## License

MIT License

## Contact

Karthik Venkatesh - wrk.krthik@gmail.com  
GitHub: [@StylistMercurial1130](https://github.com/StylistMercurial1130)
