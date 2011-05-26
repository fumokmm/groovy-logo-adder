// [参考] http://kyle-in-jp.blogspot.com/2008/08/java2d.html
// [参考] http://d.hatena.ne.jp/toshyon/20060609/p1

import java.awt.*
import java.awt.image.*
import javax.imageio.*

class GroovyLogoAdder {
  static void main(args) {
    // イメージ作成
    BufferedImage yourImg = ImageIO.read(new File(args[0]))
    BufferedImage iconImg = ImageIO.read(this.class.getResource('/images/groovy-logo.png'))

    // リサイズ
    def p = 70 // 縮小率(%)
    int newX = yourImg.width * (p / 100)
    int newY = newX * iconImg.height / iconImg.width
    BufferedImage resizedIconImg = new BufferedImage(newX, newY, iconImg.getType())
    resizedIconImg.getGraphics().drawImage(iconImg.getScaledInstance(newX, newY, Image.SCALE_AREA_AVERAGING), 0, 0, newX, newY, null)

    // ポジションの計算
    def putPosCalc = [
      topLeft    : { [x: 0,                                    y: 0]                                      },
      topRight   : { [x: yourImg.width - resizedIconImg.width, y: 0]                                      },
      bottomLeft : { [x: 0,                                    y: yourImg.height - resizedIconImg.height] },
      bottomRight: { [x: yourImg.width - resizedIconImg.width, y: yourImg.height - resizedIconImg.height] }
    ]

    // ロゴを追加
    Graphics2D gr = yourImg.createGraphics()
    gr.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0F))
    def putPos = putPosCalc.bottomRight() // 右下
    gr.drawImage(resizedIconImg, putPos.x, putPos.y ,null)
    gr.dispose() // グラフィクスを放棄

    // 書き出し(PNG)
    ImageIO.write(yourImg, 'png', new File(args[0] + '_groovy.png'))
  }
}
