package com.quangph.base.roadmap

import android.os.Bundle
import com.quangph.base.viewmodel.ISaveState
import com.quangph.jetpack.IJetContext

/**
 * Created by QuangPH on 2020-11-30.
 */
object RoadMap: IRoadMap, ISaveState {

    private var rootNode: RoadNode? = null
    private var direction: ROAD_DIRECTION? = null

    override fun log(screen: IJetContext) {
        if (rootNode == null) {
            rootNode = RoadNode()
            rootNode!!.isAvailable = true
            rootNode!!.screenName = screen.screenName
        } else {
            if (direction == ROAD_DIRECTION.FORWARD) {
                direction = ROAD_DIRECTION.BACK
                val nextNode = RoadNode()
                nextNode.isAvailable = true
                nextNode.screenName = screen.screenName
                rootNode?.latest?.addChild(nextNode)
            }
        }
    }

    override fun finish(screen: IJetContext) {
        if (rootNode == null) return
        if (direction == ROAD_DIRECTION.FORWARD) {
            val latest = rootNode?.latest
            if (latest != null) {
                if ((latest as RoadNode).screenName?.equals(screen.screenName) == true) {
                    latest.isAvailable = false
                }
            }
        } else {
            val latest = rootNode?.latest
            if ((latest as RoadNode).screenName?.equals(screen.screenName) != true) {
                return
            }

            latest.parent?.children?.removeAt(0)

            // Remove all node which is not available, it mean the activity was finished
            var prev = rootNode?.latest as RoadNode?
            while (prev != null) {
                prev = if (!prev.isAvailable) {
                    val parent: RoadNode? = prev.parent as RoadNode?
                    parent?.children?.remove(prev)
                    parent
                } else {
                    null
                }
            }
        }
    }

    override fun traceDirection(direction: ROAD_DIRECTION) {
        RoadMap.direction = direction
    }

    override fun saveInstanceState(outState: Bundle) {
        direction?.ordinal?.let {
            outState.putInt("direction", it)
        }

        val flatNode = arrayListOf<RoadNode>()
        var nextNode = rootNode
        while (nextNode != null) {
            flatNode.add(nextNode)
            nextNode = nextNode.children?.getOrNull(0) as RoadNode?
        }
        outState.putParcelableArrayList("node", flatNode)
    }

    override fun restoreInstanceState(savedInstanceState: Bundle) {
        val ordinal = savedInstanceState.getInt("direction")
        direction = if (ordinal == 0) {
            ROAD_DIRECTION.FORWARD
        } else {
            ROAD_DIRECTION.BACK
        }

        val flatNode = savedInstanceState.getParcelableArrayList<RoadNode>("node")
        if (flatNode != null) {
            var currNode: RoadNode? = null
            while (flatNode.isNotEmpty()) {
                val first = flatNode.removeAt(0)
                if (currNode != null) {
                    currNode.addChild(first)
                } else {
                    rootNode = first
                }
                currNode = first
            }
        }
    }

    fun getSourceNode(screen: IJetContext): RoadNode? {
        var result: RoadNode? = null
        var latest = rootNode?.latest as RoadNode?
        while (latest != null) {
            if (latest.screenName?.equals(screen.screenName) == true) {
                result = latest.parent as RoadNode?
                break
            } else {
                latest = latest.parent as RoadNode?
            }
        }
        return result
    }

    fun getAvailableSourceNode(screen: IJetContext): RoadNode? {
        var result: RoadNode? = null
        var curr = rootNode?.latest as RoadNode?
        while (curr != null) {
            if (curr.screenName?.equals(screen.screenName) == true) {
                break
            } else {
                curr = curr.parent as RoadNode?
            }
        }

        result = curr?.parent as RoadNode?
        while (result != null) {
            if (result.isAvailable) {
                break
            } else {
                result = result.parent as RoadNode?
            }
        }
        return result
    }

    /**
     * Just for debug propose
     */
    fun printTrace(): String {
        if (rootNode == null) {
            return ""
        }

        var next: RoadNode? = rootNode!!
        var result = ""
        while (next != null) {
            result += next.screenName
            if (next.childCount > 0) {
                result += "-->"
                next = (next.children!![0] as RoadNode?)!!
            } else {
                next = null
            }
        }
        return result
    }

    fun printAvailableTrace(): String {
        if (rootNode == null) {
            return ""
        }

        var next: RoadNode? = rootNode!!
        var result = ""
        while (next != null) {
            if (next.isAvailable) {
                result += next.screenName
            }

            if (next.childCount > 0) {
                result += "-->"
                next = (next.children!![0] as RoadNode?)!!
            } else {
                next = null
            }
        }
        return result
    }
}