import com.soywiz.kds.iterators.fastForEach
import com.soywiz.korge.view.CollisionKind
import com.soywiz.korge.view.View
import com.soywiz.korio.lang.threadLocal
import com.soywiz.korma.geom.Angle
import com.soywiz.korma.geom.Matrix
import com.soywiz.korma.geom.Point
import com.soywiz.korma.geom.Rectangle
import com.soywiz.korma.geom.cosine
import com.soywiz.korma.geom.degrees
import com.soywiz.korma.geom.plus
import com.soywiz.korma.geom.shape.Shape2d
import com.soywiz.korma.geom.times

private class MyCollisionContext {
    val tempMat = Matrix()
    val tempRect1 = Rectangle()
    val tempRect2 = Rectangle()

    val lmat = Matrix()
    val rmat = Matrix()
}

private val myCollisionContext by threadLocal { MyCollisionContext() }

private fun MyCollisionContext.collidesWith(left: View, right: View, kind: CollisionKind): Boolean {
    left.getGlobalBounds(tempRect1)
    right.getGlobalBounds(tempRect2)
    if (!tempRect1.intersects(tempRect2)) return false
    if (kind == CollisionKind.SHAPE) {
        val leftShape = left.hitShape2d
        val rightShape = right.hitShape2d
        val ml = left.getGlobalMatrixWithAnchor(lmat)
        val mr = right.getGlobalMatrixWithAnchor(rmat)
        //println("intersects[$result]: left=$leftShape, right=$rightShape, ml=$ml, mr=$mr")
        return Shape2d.intersects(leftShape, ml, rightShape, mr, tempMat)
    }
    return true
}

fun View.collidesWith(otherList: List<View>, kind: CollisionKind = CollisionKind.GLOBAL_RECT): View? {
    val ctx = myCollisionContext
    otherList.fastForEach { other ->
        if (ctx.collidesWith(this, other, kind)) return other
    }
    return null
}

fun View.moveWithCollisionsFixed(
    collision: List<View>,
    dx: Double,
    dy: Double,
    kind: CollisionKind = CollisionKind.SHAPE
): View? {
    val char = this
    val deltaXY = Point(dx, dy)
    val angle = Angle.between(0.0, 0.0, deltaXY.x, deltaXY.y)
    val length = deltaXY.length
    val oldX = char.x
    val oldY = char.y

    var view: View? = null
    arrayOf(0.degrees).fastForEach { dangle ->
        arrayOf(+1.0, -1.0).fastForEach { dscale ->
            val rangle = angle + dangle * dscale
            val lengthScale = dangle.cosine
            val dpoint = Point.fromPolar(rangle, length * lengthScale)
            char.x = oldX + dpoint.x
            char.y = oldY + dpoint.y
            //char.hitTestView(collision, kind)
            //if (!char.collidesWith(collision, kind)) {
            view = char.collidesWith(collision, kind)
            if (view == null) {
                //if (char.hitTestView(collision) == null) {
                return null
            }
        }
    }
    char.x = oldX
    char.y = oldY

    return view
}