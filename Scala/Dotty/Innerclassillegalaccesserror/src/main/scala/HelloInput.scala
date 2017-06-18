import com.jme3.app.SimpleApplication
import com.jme3.material.Material
import com.jme3.math.Vector3f
import com.jme3.scene.Geometry
import com.jme3.scene.shape.Box
import com.jme3.math.ColorRGBA
import com.jme3.input.KeyInput
import com.jme3.input.MouseInput
import com.jme3.input.controls.ActionListener
import com.jme3.input.controls.AnalogListener
import com.jme3.input.controls.KeyTrigger
import com.jme3.input.controls.MouseButtonTrigger

/** Sample 5 - how to map keys and mousebuttons to actions */
class HelloInput extends SimpleApplication {

  protected var player: Geometry = new Geometry("Player", new Box(1, 1, 1))
  var isRunning: Boolean = true

  override def simpleInitApp: Unit = {
    val mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md")
    mat.setColor("Color", ColorRGBA.Blue)
    player.setMaterial(mat)
    rootNode.attachChild(player)
    initKeys() // load my custom keybinding
  }

  /** Custom Keybinding: Map named actions to inputs. */
  def initKeys(): Unit = {
    // You can map one or several inputs to one named action
    inputManager.addMapping("Left",   new KeyTrigger(KeyInput.KEY_J))
    inputManager.addMapping("Right",  new KeyTrigger(KeyInput.KEY_K))
    // Add the names to the action listener.
    inputManager.addListener(analogListener, "Left", "Right")
  }


  private val analogListener = new AnalogListener {
    def onAnalog(name: String, value: Float, tpf: Float): Unit = {
      if (isRunning) {
        name match {
          case "Right" =>
            val v = player.getLocalTranslation
            player.setLocalTranslation(v.x + value*speed, v.y, v.z)
          case "Left" =>
            val v = player.getLocalTranslation
            player.setLocalTranslation(v.x - value*speed, v.y, v.z)
          case _ => ()
        }
      }
    }
  }
}

object HelloInput {
  def main(args:Array[String]): Unit = {
    val app = new HelloInput
    app.setShowSettings(false)
    app.start()
  }
}
