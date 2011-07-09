// cf. http://kyle-in-jp.blogspot.com/2008/08/java2d.html
// cf. http://d.hatena.ne.jp/toshyon/20060609/p1

import java.awt.*
import java.awt.image.*
import javax.imageio.*

class GroovyLogoAdder {
  static def OUTPUT_FORMAT = 'png'

  BufferedImage iconImg = ImageIO.read(this.class.getResource('/images/groovy-logo.png'))

  void makeImage(File file, Position pos, int ratio) {
    // イメージ作成
    BufferedImage yourImg = ImageIO.read(file)

    // リサイズ
    int newX = yourImg.width * (ratio / 100)
    int newY = newX * iconImg.height / iconImg.width
    BufferedImage resizedIconImg = new BufferedImage(newX, newY, iconImg.getType())
    resizedIconImg.getGraphics().drawImage(iconImg.getScaledInstance(newX, newY, Image.SCALE_AREA_AVERAGING), 0, 0, newX, newY, null)

    // ポジションの計算
    def putPosCalc = [
      (Position.topLeft)     : { [x: 0,                                    y: 0]                                      },
      (Position.topRight)    : { [x: yourImg.width - resizedIconImg.width, y: 0]                                      },
      (Position.bottomLeft)  : { [x: 0,                                    y: yourImg.height - resizedIconImg.height] },
      (Position.bottomRight) : { [x: yourImg.width - resizedIconImg.width, y: yourImg.height - resizedIconImg.height] },
      (Position.center)      : { [x: (yourImg.width / 2 - resizedIconImg.width / 2) as int,
                                  y: (yourImg.height / 2 - resizedIconImg.height / 2) as int] }
    ]

    // ロゴを追加
    Graphics2D gr = yourImg.createGraphics()
    gr.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0F))
    def putPos = putPosCalc[pos]() // 指定ポジション
    gr.drawImage(resizedIconImg, putPos.x, putPos.y ,null)
    gr.dispose() // グラフィクスを放棄

    // 書き出し(PNG)
    def resultFile = new File(file.name + '_groovy.' + OUTPUT_FORMAT)
    ImageIO.write(yourImg, OUTPUT_FORMAT, resultFile)
    println "${resultFile.name} created."
  }
}
