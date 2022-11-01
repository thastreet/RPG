import com.soywiz.korge.view.SpriteAnimation

fun SpriteAnimation.adjust(): SpriteAnimation =
    SpriteAnimation(
        sprites = listOf(sprites[0], sprites[1], sprites[2], sprites[1])
    )