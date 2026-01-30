package com.example.bird.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.jface.dialogs.MessageDialog;
import com.example.bird.client.BirdServiceClient;

/**
 * Main UI View for the Bird Monitoring System.
 */
public class BirdView extends ViewPart {
    public static final String ID = "com.example.bird.ui.views.BirdView";

    private BirdServiceClient client;
    
    private Table birdTable;
    private Table sightingTable;
    private Text birdNameInput, colorInput, weightInput, heightInput;
    private Text sBirdIdInput, locationInput, dateTimeInput;
    private Text searchBirdNameInput;

    @Override
    public void createPartControl(Composite parent) {
        try {
            this.client = new BirdServiceClient();
        } catch (Exception e) {
            Label errorLabel = new Label(parent, SWT.WRAP);
            errorLabel.setText("Failed to initialize Service Client: " + e.getMessage());
            return;
        }

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

        birdTable = new Table(composite, SWT.BORDER | SWT.FULL_SELECTION);
        birdTable.setHeaderVisible(true);
        birdTable.setLinesVisible(true);
        birdTable.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));

        String[] titles = { "Status", "Response Content", "", "", "" };
        for (String title : titles) {
            TableColumn column = new TableColumn(birdTable, SWT.NONE);
            column.setText(title);
            column.setWidth(150);
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

        Composite searchComp = new Composite(composite, SWT.NONE);
        searchComp.setLayout(new GridLayout(3, false));
        searchComp.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 2, 1));

        new Label(searchComp, SWT.NONE).setText("Filter by Bird Name:");
        searchBirdNameInput = new Text(searchComp, SWT.BORDER);
        searchBirdNameInput.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        
        Button btnSearch = new Button(searchComp, SWT.PUSH);
        btnSearch.setText("Search");
        btnSearch.addListener(SWT.Selection, e -> refreshSightings());

        sightingTable = new Table(composite, SWT.BORDER | SWT.FULL_SELECTION);
        sightingTable.setHeaderVisible(true);
        sightingTable.setLinesVisible(true);
        sightingTable.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));

        String[] titles = { "Status", "Response Content", "", "" };
        for (String title : titles) {
            TableColumn column = new TableColumn(sightingTable, SWT.NONE);
            column.setText(title);
            column.setWidth(150);
        }
    }

    private void handleAddBird() {
        if (client == null) return;
        try {
            String json = String.format("{\"name\":\"%s\",\"color\":\"%s\",\"weight\":%s,\"height\":%s}",
                birdNameInput.getText(), colorInput.getText(), weightInput.getText(), heightInput.getText());
            client.postBird(json);
            showInfo("Success", "Bird added successfully");
            refreshBirds();
        } catch (Exception e) {
            showError("Error", "Failed to add bird: " + e.getMessage());
        }
    }

    private void handleAddSighting() {
        if (client == null) return;
        try {
            String json = String.format("{\"birdId\":%s,\"location\":\"%s\",\"dateTime\":\"%s\"}",
                sBirdIdInput.getText(), locationInput.getText(), dateTimeInput.getText());
            client.postSighting(json);
            showInfo("Success", "Sighting added successfully");
            refreshSightings();
        } catch (Exception e) {
            showError("Error", "Failed to add sighting: " + e.getMessage());
        }
    }

    private void refreshBirds() {
        if (client == null) return;
        new Thread(() -> {
            try {
                final String response = client.getBirds();
                Display.getDefault().asyncExec(() -> {
                    if (birdTable == null || birdTable.isDisposed()) return;
                    birdTable.removeAll();
                    new TableItem(birdTable, SWT.NONE).setText(new String[] {"Success", response, "", "", ""});
                });
            } catch (Exception e) {
                Display.getDefault().asyncExec(() -> {
                    if (birdTable == null || birdTable.isDisposed()) return;
                    birdTable.removeAll();
                    new TableItem(birdTable, SWT.NONE).setText(new String[] {"Error", e.getMessage(), "", "", ""});
                });
            }
        }).start();
    }

    private void refreshSightings() {
        if (client == null) return;
        new Thread(() -> {
            try {
                final String birdName = searchBirdNameInput.getText();
                final String response = client.getSightings(birdName);
                Display.getDefault().asyncExec(() -> {
                    if (sightingTable == null || sightingTable.isDisposed()) return;
                    sightingTable.removeAll();
                    new TableItem(sightingTable, SWT.NONE).setText(new String[] {"Success", response, "", ""});
                });
            } catch (Exception e) {
                Display.getDefault().asyncExec(() -> {
                    if (sightingTable == null || sightingTable.isDisposed()) return;
                    sightingTable.removeAll();
                    new TableItem(sightingTable, SWT.NONE).setText(new String[] {"Error", e.getMessage(), "", ""});
                });
            }
        }).start();
    }

    private void showInfo(String title, String message) {
        Display.getDefault().asyncExec(() -> 
            MessageDialog.openInformation(getSite().getShell(), title, message)
        );
    }

    private void showError(String title, String message) {
        Display.getDefault().asyncExec(() -> 
            MessageDialog.openError(getSite().getShell(), title, message)
        );
    }

    @Override
    public void setFocus() {
        if (birdTable != null) birdTable.setFocus();
    }
}