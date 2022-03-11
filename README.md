# PacketManipulator
PacketManipulator is an open-source Packet API. You can process incoming and outgoing packets on the server. Its main focus is on being easily included in any plugin to intercept a player's packets.
## Example usage
Once you have created your own implementation of PacketListener, to inject it to a player just initialize an object of your implementation.
```java
new PacketListenerImpl(player);
```
To eject the listener to a player just use
```java
PacketListener.eject (player);
```
```java
public class PacketListenerImpl extends PacketListener {

    public PacketListenerImpl(@NotNull Player player) {
        super(player);
    }

    @Override
    public boolean onPacketOut(@NotNull WrappedPacket wrappedPacket) {
        return true;
    }

    @Override
    public boolean onPacketIn(@NotNull WrappedPacket wrappedPacket) {
        if(wrappedPacket.getPacketClass().getName().endsWith("PacketPlayInChat")) {
            String text = wrappedPacket.readString(0);
            if(text.contains("dirty")) {
                wrappedPacket.writeString(0, text.replace("dirty", "-----"));
            }
        }
        return true;
    }

}

```
## Credit to
<a href="https://wiki.vg/Protocol">wiki.vg/Protocol</a> for documentation on the protocol.
