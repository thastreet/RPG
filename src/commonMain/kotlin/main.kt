import com.soywiz.korge.Korge
import com.soywiz.korge.Korge.Config
import com.soywiz.korge.scene.Module
import com.soywiz.korinject.AsyncInjector

object MainModule : Module() {
    override val mainScene = MapScene::class

    override suspend fun AsyncInjector.configure() {
        mapPrototype { MapScene() }
        mapPrototype { BattleScene() }
    }
}

suspend fun main() = Korge(Config(module = MainModule))