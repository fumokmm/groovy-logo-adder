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
      def adder = new GroovyLogoAdder()
      args.each { adder.makeImage(it) }
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
              def fileNameList = t.getTransferData(DataFlavor.javaFileListFlavor)
              fileNameList.each {
                adder.makeImage(it)
              }
            }
        }
      ] as DropTarget
    }
  }
}
