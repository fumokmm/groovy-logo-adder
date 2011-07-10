// cf. http://www.ne.jp/asahi/hishidama/home/tech/java/swing/DropTarget.html

import groovy.swing.SwingBuilder

import javax.swing.JFrame
import java.awt.dnd.DropTarget
import java.awt.dnd.DnDConstants
import java.awt.datatransfer.DataFlavor
import java.awt.datatransfer.Transferable
import java.awt.FlowLayout
import javax.swing.BoxLayout

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
                            title: 'Groovy Logo Adder v0.11',
                            defaultCloseOperation: JFrame.EXIT_ON_CLOSE,
                            pack : true,
                            show : true) {
      panel {
        boxLayout(axis: BoxLayout.Y_AXIS)
        panel {
          label(text: 'Please Drag & Drop your icon file here.')
        }
        def rGroup = buttonGroup()
        panel {
          boxLayout(axis: BoxLayout.X_AXIS)
          radioButton(id: 'rTL', 'topLeft', buttonGroup: rGroup)
          radioButton(id: 'rTR', 'topRight', buttonGroup: rGroup)
          radioButton(id: 'rBL', 'bottomLeft', buttonGroup: rGroup)
          radioButton(id: 'rBR', 'bottomRight', buttonGroup: rGroup, selected: true)
          radioButton(id: 'rCN', 'center', buttonGroup: rGroup)
        }
        panel(layout: new FlowLayout()) {
          label('Reduction Ratio')
          comboBox(id: 'ratio', items: (1..12).collect{ "${it*10}%" }, selectedIndex: 6)
        }

        mainFrame.dropTarget = [
          drop: { dtde ->
            def t = dtde.transferable
            if (t.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
              dtde.acceptDrop(DnDConstants.ACTION_REFERENCE)
              def adder = new GroovyLogoAdder()
              def fileList = t.getTransferData(DataFlavor.javaFileListFlavor)
              def pos = [rTL, rTR, rBL, rBR, rCN].findAll{ it.selected }.first().text as Position
              def ratio = ratio.selectedItemReminder - '%' as int
              fileList.each { File f ->
                adder.makeImage(f, pos, ratio)
              }
            }
          }
        ] as DropTarget
      }
    }
  }
}
