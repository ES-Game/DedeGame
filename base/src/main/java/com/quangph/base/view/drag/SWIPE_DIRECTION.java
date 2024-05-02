package com.quangph.base.view.drag;

/**
 * Created by QuangPH on 2020-09-17.
 */
public enum SWIPE_DIRECTION {
    LEFT, UP, RIGHT, DOWN;

    public static SWIPE_DIRECTION fromAngle(double angle) {
        if(inRange(angle, 45, 135)){
            return SWIPE_DIRECTION.UP;
        }
        else if(inRange(angle, 0,45) || inRange(angle, 315, 360)){
            return SWIPE_DIRECTION.RIGHT;
        }
        else if(inRange(angle, 225, 315)){
            return SWIPE_DIRECTION.DOWN;
        }
        else{
            return SWIPE_DIRECTION.LEFT;
        }
    }

    public static SWIPE_DIRECTION directionXFromAngle(double angle) {
        if (inRange(angle,0, 90) || inRange(angle, 270, 360)) {
            return SWIPE_DIRECTION.RIGHT;
        } else {
            return SWIPE_DIRECTION.LEFT;
        }
    }

    private static boolean inRange(double angle, float init, float end){
        return (angle >= init) && (angle < end);
    }
}
