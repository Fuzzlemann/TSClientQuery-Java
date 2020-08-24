# TSClientQuery Java

![Build badge](https://ci.fuzzlemann.de/job/TSClientQuery-Java/badge/icon)

An interface for the TeamSpeak 3 Client Query written in Java.

### Before using, do understand that the implemented commands are still heavily limited!

### Features
* Queued & asynchronous command execution
* Automatic loading of the API key (not guaranteed to work on each system)
* Event-based system for e.g. incoming messages

### Download
You are able to download the latest build from [here](https://ci.fuzzlemann.de/job/TSClientQuery-Java/).

### Usage
*!!! The usage of this API is likely to change in following releases. !!!*

1. Get the API key for the plugin automatically or via user-input. In order to get it automatically, instantiate `TSAPIKeyLoader` and call `loadAPIKey()`. If it is unable to extract it, it will return an empty `Optional`.
2. Create an instance of `TSClientQueryConfiguration`. For example: `new TSClientQueryConfiguration("127.0.0.1", 25639, apiKey)`
3. Set the configuration using `TSClientQuery.setConfiguration(configuration)`
4. You're now able to get the instance of the ClientQuery through `TSClientQuery.getInstance()`. The query connects after the first call.

#### Commands
Commands are executed by instantiating the command class and then calling `BaseCommand#execute()` or `BaseCommand#getResponse()`. 
Keep in mind, `BaseCommand#getResponse()` blocks the current Thread until the command was executed and the response was received and processed.
An alternative for `BaseCommand#getResponse()` is calling `BaseCommand#execute()` and using the `Future` returned by `BaseCommand#getResponseFuture()`.

##### Example:

```
ClientListCommand clientListCommand = new ClientListCommand();

for (Client client : clientListCommand.getResponse().getClientList()) {
    new SendTextMessageCommand(client, "Your client ID is " + client.getClientID()).execute();
}
```

#### Events
In order to listen for e.g. incoming chat messages, create a class which implements `TSListener`.
In this class, create a method which has the event to listen to as the only parameter (e.g. `ClientMessageReceivedEvent`) and annotate the method with `@EventHandler`.
Finally, register the class with the listener using `TSEventHandler.registerListener(instance)`.

The method is now called every time the event's being triggered.

##### Example:

```
public class ExampleListener implements TSListener {
    
    @EventHandler
    public void onMessageReceived(ClientMessageReceivedEvent e) {
        System.out.println("Received an message: " + e.getMessage());
    }
    
}
```

### Planned
* Implementing most features provided by the client query API
