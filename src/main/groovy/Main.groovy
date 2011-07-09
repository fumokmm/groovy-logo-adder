// cf. http://www.ne.jp/asahi/hishidama/home/tech/java/swing/DropTarget.html

import groovy.swing.SwingBuilder

import javax.swing.JFrame
import java.awt.dnd.DropTarget
import java.awt.dnd.DnDConstants
import java.awt.datatransfer.DataFlavor
import java.awt.datatransfer.Transferable

class Main {

  static void main(args) {
    if (args) {
      def analyze = {
        def pos = args.grep(~/((top|bottom)(Right|Left)|center)/)
        def ratio = args.grep(~/\d+\%/)
        [pos    : (pos ? pos.first() : 'bottomRight') as Position,
         ratio  : ratio ? ratio.first() - '%' as int : 70,
         images : args - pos - ratio ]
      }
      def adder = new GroovyLogoAdder()
      analyze().with{ rslt ->
        rslt.images.each {
          adder.makeImage(new File(it), rslt.pos, rslt.ratio)
        }
      }
    } else {
      performGUI()
    }
  }

  static void performGUI() {
    def swing = new SwingBuilder()
    def frame = swing.frame(id   : 'mainFrame',
                            title: 'Groovy Logo Adder',
                            defaultCloseOperation: JFrame.EXIT_ON_CLOSE,
                            show : true,
                            size : [300, 100]) {
      label(text: 'Please Drag & Drop your icon file here.')
      mainFrame.dropTarget = [
        drop: { dtde ->
            def t = dtde.transferable
            if (t.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
              dtde.acceptDrop(DnDConstants.ACTION_REFERENCE)
              def adder = new GroovyLogoAdder()
              def fileList = t.getTransferData(DataFlavor.javaFileListFlavor)
              fileList.each { File f ->
                adder.makeImage(f, 'bottomRight' as Position, 70)
              }
            }
        }
      ] as DropTarget
    }
  }
}
