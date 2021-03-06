package li.cil.oc.server.command

import li.cil.oc.api.Network
import li.cil.oc.common.command.SimpleCommand
import li.cil.oc.server.network.DebugNetwork
import net.minecraft.command.ICommandSender
import net.minecraft.command.WrongUsageException
import net.minecraft.server.MinecraftServer

object SendDebugMessageCommand extends SimpleCommand("oc_sendDebugMessage") {
  aliases += "oc_sdbg"

  override def getUsage(sender: ICommandSender): String = name + "<destinationAddress> [message...]"

  override def execute(server: MinecraftServer, sender: ICommandSender, args: Array[String]): Unit = {
    if (args == null || args.length == 0) {
      throw new WrongUsageException("no destination address specified.")
    }
    val destination = args(0)
    DebugNetwork.getEndpoint(destination).foreach { endpoint =>
      val packet = Network.newPacket(sender.getName, destination, 0, args.drop(1).toList.toArray[AnyRef])
      endpoint.receivePacket(packet)
    }
  }

  override def getRequiredPermissionLevel = 2
}
