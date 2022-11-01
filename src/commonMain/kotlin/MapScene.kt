import com.soywiz.klock.milliseconds
import com.soywiz.korge.scene.Scene
import com.soywiz.korge.view.SContainer
import com.soywiz.korge.view.Sprite
import com.soywiz.korge.view.addFixedUpdater
import com.soywiz.korge.view.hitShape
import com.soywiz.korge.view.image
import com.soywiz.korge.view.xy
import com.soywiz.korim.bitmap.sliceWithSize
import com.soywiz.korim.format.readBitmap
import com.soywiz.korio.async.launchImmediately
import com.soywiz.korio.file.std.resourcesVfs
import com.soywiz.korma.geom.vector.rect

class MapScene : Scene() {
    override suspend fun SContainer.sceneInit() {
        val hero = resourcesVfs["sprites.png"].readBitmap()
        val tileset = resourcesVfs["tileset.png"].readBitmap().apply {
            clampX(8)
            clampY(158)
            clampWidth(80)
            clampHeight(70)
        }
        val center = tileset.sliceWithSize(8, 158, 80, 70)

        val house = Sprite(center).apply {
            xy(20.0, 20.0)
            hitShape {
                rect(0.0, 0.0, 80.0, 70.0)
            }
        }

        val grass = tileset.sliceWithSize(23, 64, 16, 16)

        val player1 = PlayerCharacter(
            bitmap = hero,
            collisions = listOf(house),
            handleCollision = {
                if (it == house) {
                    launchImmediately { sceneContainer.changeTo<BattleScene>() }
                }
            }
        ).apply {
            xy(100.0, 100.0)
            scale = 0.5
            hitShape {
                rect(11, 30, 38, 28)
            }
        }

        for (i in 0..50) {
            for (j in 0..50) {
                image(grass).apply {
                    xy(i * 16, j * 16)
                }
            }
        }

        addChild(house)
        addChild(player1)

        addFixedUpdater(64.milliseconds, true) {
            player1.handleKeys(keys)
        }
    }
}
