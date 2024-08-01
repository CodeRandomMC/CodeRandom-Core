# CodeRandomCore

CodeRandomCore is a Bukkit plugin designed to manage various aspects of a Minecraft server, including player UUIDs, MySQL database interactions, and messaging utilities. This guide will help you set up and use the API provided by the plugin.

## Table of Contents
- [Installation](#installation)
- [Configuration](#configuration)
- [Usage](#usage)
  - [UUID Management](#uuid-management)
  - [MySQL Management](#mysql-management)
  - [Messaging Utilities](#messaging-utilities)
  - [Title Utilities](#title-utilities)
  - [ActionBar Utilities](#actionbar-utilities)
- [Contributing](#contributing)
- [License](#license)

## Installation

1. Download the latest release of the CodeRandomCore plugin.
2. Place the plugin JAR file in your server's `plugins` directory.
3. Start your Minecraft server to generate the default configuration files.

## Configuration

The plugin uses a configuration file located at `plugins/CodeRandomCore/config.yml`. Here is an example of the MySQL configuration section:

```yaml
MySQL:
  enabled: false
  host: localhost
  port: 3306
  database: code_random
  username: root
  password: ""
```

### Configuration Options

- `enabled`: Set to `true` to enable MySQL support.
- `host`: The hostname or IP address of your MySQL server.
- `port`: The port number of your MySQL server (default is 3306).
- `database`: The name of the database to use.
- `username`: The username for the MySQL connection.
- `password`: The password for the MySQL connection.

## Usage

### UUID Management

CodeRandomCore provides utilities for managing player UUIDs, especially for Bedrock players using Floodgate.

#### Fetching UUIDs

To fetch a player's UUID:

```
UUID uuid = UUIDFetcher.getUUID("playerName");
```

This method will automatically determine if the player is a Bedrock player, an online player, or an offline player, and fetch the UUID accordingly.

### MySQL Management

The plugin uses HikariCP for managing MySQL connections.

#### Initializing MySQL

Ensure that MySQL is enabled in your `config.yml` file. The MySQLManager class manages the connection pool:

```
MySQLManager.initialize(plugin);
MySQLManager mysqlManager = MySQLManager.getInstance();

if (mysqlManager.connect()) {
    // Connected successfully
} else {
    // Connection failed
}
```

#### Executing Queries

To execute queries:

```
String query = "SELECT * FROM players";
try (ResultSet rs = mysqlManager.executeQuery(query)) {
    while (rs.next()) {
        // Process the result set
    }
} catch (SQLException e) {
    e.printStackTrace();
}
```

### Messaging Utilities

The plugin provides utilities for sending formatted messages to players.

#### Sending Messages

To send a formatted message:

```
MessageUtils.formattedMessage(player, "Your message here");
```

To send a message with a title and lines:

```
MessageUtils.messageWithTitle(player, "Title", "Line 1", "Line 2");
```

### Title Utilities

To send titles to players:

```
TitleUtils.title(player, "Main Title", "Subtitle", 20, 100, 20);
```

This will display a title with a fade-in time of 20 ticks, stay time of 100 ticks, and fade-out time of 20 ticks.

### ActionBar Utilities

To send an action bar message:

```
ActionBarUtils.actionBar(player, "ActionBar Message", 60);
```

This will display an action bar message for 3 seconds (60 ticks).

## Contributing

1. Fork the repository.
2. Create your feature branch (`git checkout -b feature/fooBar`).
3. Commit your changes (`git commit -am 'Add some fooBar'`).
4. Push to the branch (`git push origin feature/fooBar`).
5. Create a new Pull Request.

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.