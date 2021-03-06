package studio.forface.bottomappbar.materialbottomdrawer.drawer

import android.widget.ImageView
import studio.forface.bottomappbar.materialbottomdrawer.draweritems.BaseDrawerItem
import studio.forface.bottomappbar.materialbottomdrawer.draweritems.DrawerItem
import studio.forface.bottomappbar.materialbottomdrawer.holders.*
import studio.forface.bottomappbar.materialbottomdrawer.params.*
import studio.forface.bottomappbar.utils.Drawables
import java.util.*

class MaterialDrawer(
    _header: Header? = null,
    _body: Body? = null
): Observable() {

    var header = _header
        set(value) {
            field = value
            setChanged()
            notifyObservers()
        }
    var body = _body
        set(value) {
            field = value
            setChanged()
            notifyObservers()
        }

    class Header:
            Background<Header>,
            Title<Header>,
            Icon<Header>
    {
        override val thisRef: Header get() = this

        override var backgroundColorHolder =            ColorHolder()
        override var backgroundCornerRadiusSizeHolder =  SizeHolder()

        override var titleTextHolder =                  TextHolder()
        override var titleTextStyleHolder =             TextStyleHolder()
        override var titleTextSizeHolder =              TextSizeHolder()
        override var titleColorHolder =                 ColorHolder()

        override var iconImageHolder =                  ImageHolder()
        override var iconColorHolder =                  ColorHolder()
        override var iconSizeHolder =                   SizeHolder()

        private var hasCustomShape = false

        override fun withIconShape( imageShape: ImageShape ) {
            super.withIconShape( imageShape )
            hasCustomShape = true
        }

        override fun applyIconTo( imageView: ImageView, applyOrHide: Boolean ) {
            if ( ! hasCustomShape ) iconImageHolder.imageShape = ImageShape.ROUND
            super.applyIconTo( imageView,true )
        }
    }

    class Body(
        _items: List<DrawerItem> = listOf()
    ): Observable(), Selection<Body> {
        override val thisRef: Body get() = this

        override var selectionColorHolder =             ColorHolder()
        override var selectionCornerRadiusSizeHolder =  SizeHolder( dp = Drawables.CORNER_RADIUS_SOFT )

        override var onItemClickListener: OnItemClickListener = { _, _ ->  }

        var items = _items
            set( value ) {
                field = value
                setChanged()
                notifyObservers()
            }

        fun items( items: List<DrawerItem> ) =
                apply { this.items = items }

        fun setSelected( selectedId: Int ) = apply {
            items = items.mapBaseDrawerItems { it.selected = it.id == selectedId && it.selectable }
        }

    }
}

fun List<DrawerItem>.mapBaseDrawerItems( mapper: (BaseDrawerItem) -> Unit ) =
        this.map { ( it as? BaseDrawerItem )?.apply { mapper( this ) } ?: it }

fun List<DrawerItem>.forEachBaseDrawerItem( block: (BaseDrawerItem) -> Unit ) {
    forEach { ( it as? BaseDrawerItem )?.run( block ) }
}