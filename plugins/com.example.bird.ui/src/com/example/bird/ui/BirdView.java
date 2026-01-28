package com.example.bird.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.part.ViewPart;
import com.example.bird.client.BirdServiceClient;

/**
 * Main UI View for the Bird Monitoring System.
 * Provides forms for adding birds and sightings, and tables for displaying data.
 */
public class BirdView extends ViewPart {
    public static final String ID = "com.example.bird.ui.views.BirdView";

    private BirdServiceClient client = new BirdServiceClient();
    
    private Table birdTable;
    private Table sightingTable;
    private Text birdNameInput, colorInput, weightInput, heightInput;
    private Text sBirdIdInput, locationInput, dateTimeInput;
    private Text searchBirdNameInput;

    @Override
    public void createPartControl(Composite parent) {
        TabFolder folder = new TabFolder(parent, SWT.NONE);

        createBirdTab(folder);
        createSightingTab(folder);
    }

    private void createBirdTab(TabFolder folder) {
        TabItem item = new TabItem(folder, SWT.NONE);
        item.setText("Birds");

        Composite composite = new Composite(folder, SWT.NONE);
        composite.setLayout(new GridLayout(2, false));
        item.setControl(composite);

        // --- Add Bird Form ---
        Group addGroup = new Group(composite, SWT.NONE);
        addGroup.setText("Add New Bird");
        addGroup.setLayout(new GridLayout(2, false));
        addGroup.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 2, 1));

        new Label(addGroup, SWT.NONE).setText("Name:");
        birdNameInput = new Text(addGroup, SWT.BORDER);
        birdNameInput.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        new Label(addGroup, SWT.NONE).setText("Color:");
        colorInput = new Text(addGroup, SWT.BORDER);
        colorInput.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        new Label(addGroup, SWT.NONE).setText("Weight (g):");
        weightInput = new Text(addGroup, SWT.BORDER);
        weightInput.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        new Label(addGroup, SWT.NONE).setText("Height (cm):");
        heightInput = new Text(addGroup, SWT.BORDER);
        heightInput.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        Button btnAddBird = new Button(addGroup, SWT.PUSH);
        btnAddBird.setText("Save Bird");
        btnAddBird.addListener(SWT.Selection, e -> handleAddBird());

        // --- Bird List Table ---
        birdTable = new Table(composite, SWT.BORDER | SWT.FULL_SELECTION);
        birdTable.setHeaderVisible(true);
        birdTable.setLinesVisible(true);
        birdTable.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));

        String[] titles = { "ID", "Name", "Color", "Weight", "Height" };
        for (String title : titles) {
            TableColumn column = new TableColumn(birdTable, SWT.NONE);
            column.setText(title);
            column.setWidth(100);
        }

        Button btnRefresh = new Button(composite, SWT.PUSH);
        btnRefresh.setText("Refresh Bird List");
        btnRefresh.addListener(SWT.Selection, e -> refreshBirds());
    }

    private void createSightingTab(TabFolder folder) {
        TabItem item = new TabItem(folder, SWT.NONE);
        item.setText("Sightings");

        Composite composite = new Composite(folder, SWT.NONE);
        composite.setLayout(new GridLayout(2, false));
        item.setControl(composite);

        // --- Add Sighting Form ---
        Group addGroup = new Group(composite, SWT.NONE);
        addGroup.setText("Add Sighting");
        addGroup.setLayout(new GridLayout(2, false));
        addGroup.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 2, 1));

        new Label(addGroup, SWT.NONE).setText("Bird ID:");
        sBirdIdInput = new Text(addGroup, SWT.BORDER);
        sBirdIdInput.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        new Label(addGroup, SWT.NONE).setText("Location:");
        locationInput = new Text(addGroup, SWT.BORDER);
        locationInput.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        new Label(addGroup, SWT.NONE).setText("Date/Time (ISO):");
        dateTimeInput = new Text(addGroup, SWT.BORDER);
        dateTimeInput.setMessage("yyyy-MM-ddTHH:mm:ss");
        dateTimeInput.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        Button btnAddSighting = new Button(addGroup, SWT.PUSH);
        btnAddSighting.setText("Save Sighting");
        btnAddSighting.addListener(SWT.Selection, e -> handleAddSighting());

        // --- Search/Filter ---
        Composite searchComp = new Composite(composite, SWT.NONE);
        searchComp.setLayout(new GridLayout(3, false));
        searchComp.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 2, 1));

        new Label(searchComp, SWT.NONE).setText("Filter by Bird Name:");
        searchBirdNameInput = new Text(searchComp, SWT.BORDER);
        searchBirdNameInput.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        
        Button btnSearch = new Button(searchComp, SWT.PUSH);
        btnSearch.setText("Search");
        btnSearch.addListener(SWT.Selection, e -> refreshSightings());

        // --- Sightings List Table ---
        sightingTable = new Table(composite, SWT.BORDER | SWT.FULL_SELECTION);
        sightingTable.setHeaderVisible(true);
        sightingTable.setLinesVisible(true);
        sightingTable.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));

        String[] titles = { "ID", "Bird", "Location", "Date/Time" };
        for (String title : titles) {
            TableColumn column = new TableColumn(sightingTable, SWT.NONE);
            column.setText(title);
            column.setWidth(120);
        }
    }

    private void handleAddBird() {
        try {
            String json = String.format("{\"name\":\"%s\",\"color\":\"%s\",\"weight\":%s,\"height\":%s}",
                birdNameInput.getText(), colorInput.getText(), weightInput.getText(), heightInput.getText());
            client.postBird(json);
            MessageDialog.openInformation(null, "Success", "Bird added successfully");
            refreshBirds();
        } catch (Exception e) {
            MessageDialog.openError(null, "Error", "Failed to add bird: " + e.getMessage());
        }
    }

    private void handleAddSighting() {
        try {
            String json = String.format("{\"birdId\":%s,\"location\":\"%s\",\"dateTime\":\"%s\"}",
                sBirdIdInput.getText(), locationInput.getText(), dateTimeInput.getText());
            client.postSighting(json);
            MessageDialog.openInformation(null, "Success", "Sighting added successfully");
            refreshSightings();
        } catch (Exception e) {
            MessageDialog.openError(null, "Error", "Failed to add sighting: " + e.getMessage());
        }
    }

    private void refreshBirds() {
        try {
            String response = client.getBirds();
            birdTable.removeAll();
            // Note: In a production app, use a JSON parser like Jackson/Gson.
            // This is a simplified display of raw response for demonstration.
            TableItem item = new TableItem(birdTable, SWT.NONE);
            item.setText(new String[] {"Raw Data", response, "", "", ""});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void refreshSightings() {
        try {
            String response = client.getSightings(searchBirdNameInput.getText());
            sightingTable.removeAll();
            TableItem item = new TableItem(sightingTable, SWT.NONE);
            item.setText(new String[] {"Raw Data", response, "", ""});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setFocus() {
        birdTable.setFocus();
    }
}

/**
 * Helper class for simple dialogs.
 */
class MessageDialog {
    public static void openInformation(Shell shell, String title, String message) {
        MessageBox box = new MessageBox(Display.getDefault().getActiveShell(), SWT.ICON_INFORMATION | SWT.OK);
        box.setText(title);
        box.setMessage(message);
        box.open();
    }
    public static void openError(Shell shell, String title, String message) {
        MessageBox box = new MessageBox(Display.getDefault().getActiveShell(), SWT.ICON_ERROR | SWT.OK);
        box.setText(title);
        box.setMessage(message);
        box.open();
    }
}