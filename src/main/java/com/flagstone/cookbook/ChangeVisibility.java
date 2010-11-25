package com.flagstone.cookbook;
/*
 * ChangeVisibility.java
 * Cookbook
 *
 * Copyright (c) 2001-2010 Flagstone Software Ltd. All rights reserved.
 *
 * This code is distributed on an 'AS IS' basis, WITHOUT WARRANTY OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, AND Flagstone HEREBY DISCLAIMS ALL SUCH
 * WARRANTIES, INCLUDING WITHOUT LIMITATION, ANY WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE, AND NONINFRINGEMENT OF THIRD PARTY RIGHTS.
 */

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.flagstone.transform.Background;
import com.flagstone.transform.Movie;
import com.flagstone.transform.MovieHeader;
import com.flagstone.transform.Place2;
import com.flagstone.transform.PlaceType;
import com.flagstone.transform.ShowFrame;
import com.flagstone.transform.datatype.Bounds;
import com.flagstone.transform.datatype.Color;
import com.flagstone.transform.datatype.ColorTransform;
import com.flagstone.transform.datatype.WebPalette;
import com.flagstone.transform.fillstyle.FillStyle;
import com.flagstone.transform.fillstyle.SolidFill;
import com.flagstone.transform.linestyle.LineStyle;
import com.flagstone.transform.linestyle.LineStyle1;
import com.flagstone.transform.shape.DefineShape;
import com.flagstone.transform.shape.Line;
import com.flagstone.transform.shape.Shape;
import com.flagstone.transform.shape.ShapeRecord;
import com.flagstone.transform.shape.ShapeStyle;

/*
 * This example shows how to change the transparency of a shape:
 *
 * To run this example, type the following on a command line:
 *
 *     java -cp ... com.flagstone.cookbook.ChangeVisibility file-out
 *
 * where
 *
 *     file-out is the path where the file will be written. If no output file
 *     is specified then a file named after the example will be written to the
 *     current directory.
*/
public class ChangeVisibility
{
    public static void main(String[] args)
    {
        try
        {
            String out = args.length == 0 ? "ChangeVisibility.swf" : args[0];
            ChangeVisibility example = new ChangeVisibility();
            Movie movie = new Movie();
            example.createMovie(movie);
            movie.encodeToFile(new File(out));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    void createMovie(Movie movie) {

        int uid = 1;
        int xLower = 0;
        int yLower = 0;
        int xUpper = 8000;
        int yUpper = 8000;

        float framesPerSecond = 12.0f;

        MovieHeader header = new MovieHeader();
        header.setFrameRate(framesPerSecond);
        header.setFrameSize(new Bounds(xLower, yLower, xUpper, yUpper));
        movie.add(header);

        movie.add(new Background(WebPalette.LIGHT_BLUE.color()));

        DefineShape rectangle = defineRectangle(uid++, WebPalette.RED.color());
        movie.add(rectangle);

        movie.add(Place2.show(rectangle.getIdentifier(), 1, 4000, 4000));
        movie.add(ShowFrame.getInstance());

        int delta = 0;
        int step = 10;
        int count = 255/step;

        /* Decrease the alpha so the object fades from view */

        for (int i=0; i<count; i++, delta -= step) {
            movie.add(new Place2().setType(PlaceType.MODIFY).setLayer(1)
                    .setColorTransform(new ColorTransform(0, 0, 0, delta)));
            movie.add(ShowFrame.getInstance());
        }

        /* Now increase it so the object reappears */

        for (int i=0; i<count; i++, delta += step) {
            movie.add(new Place2().setType(PlaceType.MODIFY).setLayer(1)
                    .setColorTransform(new ColorTransform(0, 0, 0, delta)));
            movie.add(ShowFrame.getInstance());
        }
    }

    private DefineShape defineRectangle(int identifier, Color fillColor) {

        final int width = 4000;
        final int height = 4000;
        final int lineWidth = 20;
        final Color lineColor = WebPalette.BLACK.color();

        /*
         * Create the bounding box for the shape taking into account the
         * thickness of the border.
         */
        final Bounds bounds = new Bounds(
                -(width + lineWidth) / 2, -(height + lineWidth) / 2,
                (width + lineWidth) / 2, (height + lineWidth) / 2);

        List<LineStyle> lineStyles = new ArrayList<LineStyle>();
        List<FillStyle> fillStyles = new ArrayList<FillStyle>();

        lineStyles.add(new LineStyle1(lineWidth, lineColor));
        fillStyles.add(new SolidFill(fillColor));

        /*
         * Create the outline of the shape.
         */
        List<ShapeRecord> shapeRecords = new ArrayList<ShapeRecord>();

        shapeRecords.add(new ShapeStyle().setLineStyle(1).setFillStyle(1)
                .setMove(-width/2, -height/2));
        shapeRecords.add(new Line(width, 0));
        shapeRecords.add(new Line(0, height));
        shapeRecords.add(new Line(-width, 0));
        shapeRecords.add(new Line(0, -height));

        DefineShape rectangle = new DefineShape(identifier, bounds, fillStyles,
                lineStyles, new Shape(shapeRecords));

        return rectangle;
    }
}