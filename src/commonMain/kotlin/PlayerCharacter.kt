import com.soywiz.klock.milliseconds
import com.soywiz.korev.Key
import com.soywiz.korge.input.InputKeys
import com.soywiz.korge.view.CollisionKind
import com.soywiz.korge.view.Sprite
import com.soywiz.korge.view.SpriteAnimation
import com.soywiz.korge.view.View
import com.soywiz.korim.bitmap.Bitmap

private data class KeyAssignment(
    val key: Key,
    val animation: SpriteAnimation,
    val block: () -> Unit
)

class PlayerCharacter(
    bitmap: Bitmap,
    collisions: List<View>,
    handleCollision: (View?) -> Unit,
    spriteAnimationLeft: SpriteAnimation = SpriteAnimation(
        spriteMap = bitmap,
        spriteWidth = 60,
        spriteHeight = 60,
        marginTop = 76,
        marginLeft = 0,
        columns = 3,
        rows = 1
    ).adjust(),
    spriteAnimationRight: SpriteAnimation = SpriteAnimation(
        spriteMap = bitmap,
        spriteWidth = 60,
        spriteHeight = 60,
        marginTop = 156,
        marginLeft = 0,
        columns = 3,
        rows = 1
    ).adjust(),
    spriteAnimationDown: SpriteAnimation = SpriteAnimation(
        spriteMap = bitmap,
        spriteWidth = 60,
        spriteHeight = 60,
        columns = 3,
        rows = 1
    ).adjust(),
    spriteAnimationUp: SpriteAnimation = SpriteAnimation(
        spriteMap = bitmap,
        spriteWidth = 60,
        spriteHeight = 60,
        marginTop = 236,
        columns = 3,
        rows = 1
    ).adjust()
) : Sprite(spriteAnimationRight) {

    private val assignments = listOf(
        KeyAssignment(Key.LEFT, spriteAnimationLeft) {
            handleCollision(moveWithCollisionsFixed(collisions, -4.0, 0.0, CollisionKind.SHAPE))
        },
        KeyAssignment(Key.RIGHT, spriteAnimationRight) {
            handleCollision(moveWithCollisionsFixed(collisions, 4.0, 0.0, CollisionKind.SHAPE))
        },
        KeyAssignment(Key.UP, spriteAnimationUp) {
            handleCollision(moveWithCollisionsFixed(collisions, 0.0, -4.0, CollisionKind.SHAPE))
        },
        KeyAssignment(Key.DOWN, spriteAnimationDown) {
            handleCollision(moveWithCollisionsFixed(collisions, 0.0, 4.0, CollisionKind.SHAPE))
        }
    )

    private var isMoving = false

    fun handleKeys(inputKeys: InputKeys) {
        val anyMovement = assignments.firstOrNull { inputKeys[it.key] }?.also {
            playAnimation(it.animation, spriteDisplayTime = 160.milliseconds)
            it.block()
        } != null

        if (anyMovement != isMoving) {
            if (isMoving) {
                stopAnimation()
                setFrame(1)
            }
            isMoving = anyMovement
        }
    }
}