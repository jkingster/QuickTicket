package io.jacobking.quickticket.gui.controller.impl;

import io.jacobking.quickticket.core.type.StatusType;
import io.jacobking.quickticket.gui.controller.Controller;
import javafx.fxml.FXML;
import javafx.geometry.Orientation;
import org.controlsfx.control.SegmentedBar;

import java.net.URL;
import java.util.ResourceBundle;

public class MetricsController extends Controller {

    @FXML private SegmentedBar<SegmentedBar.Segment> segmentedBar;

    @Override public void initialize(URL url, ResourceBundle resourceBundle) {
        configureSegmentBar();
    }

    private void configureSegmentBar() {
        final int totalTickets = ticket.getObservableList().size();
        final int openTickets = ticket.getListByStatus(StatusType.OPEN).size();
        final int pausedTickets = ticket.getListByStatus(StatusType.PAUSED).size();
        final int activeTickets = ticket.getListByStatus(StatusType.ACTIVE).size();
        final int resolvedTickets = ticket.getListByStatus(StatusType.RESOLVED).size();

        segmentedBar.setOrientation(Orientation.HORIZONTAL);
        segmentedBar.getSegments().addAll(
                new SegmentedBar.Segment(openTickets, String.valueOf(openTickets)),
                new SegmentedBar.Segment(pausedTickets, String.valueOf(pausedTickets)),
                new SegmentedBar.Segment(activeTickets, String.valueOf(activeTickets)),
                new SegmentedBar.Segment(resolvedTickets, String.valueOf(resolvedTickets))
        );
//
//        segmentedBar.setSegmentViewFactory(segment -> {
//            final SegmentedBar<SegmentedBar.Segment>.SegmentView view = segmentedBar.new SegmentView(segment);
//            final String color = segment.getValue() <= 50 ? "RED" : "YELLOW";
//            view.setStyle("-fx-background-color:"+color);
//            return view;
//        });
    }
}
