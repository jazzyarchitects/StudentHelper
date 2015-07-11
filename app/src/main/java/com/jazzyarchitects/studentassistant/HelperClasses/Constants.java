package com.jazzyarchitects.studentassistant.HelperClasses;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.text.Html;
import android.text.Spanned;

/**
 * Created by Jibin_ism on 08-Jul-15.
 */
public class Constants {

    public static Spanned getBunkedClassText(int bunkedClasses){
        return Html.fromHtml("Bunked Classes: <b>"+bunkedClasses+"</b>");
    }
    public static Spanned getAttendancePercentageText(double percentage){
        return Html.fromHtml("Attendance: <b>"+percentage+"%</b>");
    }
    public static Spanned getProfessorNameText(String name){
        return Html.fromHtml("Professor: <b>"+name+"</b>");
    }
    public static Spanned getAssignmentCountText(int assignmentCount){
        return Html.fromHtml("Assignments Pending: <b>"+assignmentCount+"</b>");
    }

    public static boolean isColorDark(int color){
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        return (hsv[0] > 345.0) || (hsv[0] < 15.0) || (hsv[0] > 225.0 && hsv[0] < 265.0) || (hsv[2] < 0.4);
    }





    /**
     * Image Size Reducing
     */
    public static Bitmap getScaledBitmap(String picturePath, int width, int height) {
        BitmapFactory.Options sizeOptions = new BitmapFactory.Options();
        sizeOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(picturePath, sizeOptions);

        int inSampleSize = calculateInSampleSize(sizeOptions, width, height);

        sizeOptions.inJustDecodeBounds = false;
        sizeOptions.inSampleSize = inSampleSize;

        return BitmapFactory.decodeFile(picturePath, sizeOptions);
    }

    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and
            // width
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will
            // guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        return inSampleSize;
    }
}
