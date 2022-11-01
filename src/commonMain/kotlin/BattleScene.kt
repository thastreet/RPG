import com.soywiz.korge.scene.Scene
import com.soywiz.korge.view.SContainer
import com.soywiz.korge.view.Sprite
import com.soywiz.korge.view.size
import com.soywiz.korim.format.readBitmap
import com.soywiz.korio.file.std.resourcesVfs

class BattleScene : Scene() {
    override suspend fun SContainer.sceneInit() {
        val bg = resourcesVfs["battle.png"].readBitmap()
        addChild(Sprite(bg).apply {
            size(sceneWidth, sceneHeight)
        })
    }
}
