package com.example.bird.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.part.ViewPart;
import com.example.bird.client.BirdServiceClient;

/**
 * Eclipse ViewPart providing the UI for Bird and Sighting management.
 */
public class BirdView extends ViewPart {
    private BirdServiceClient client = new BirdServiceClient();
    private Table birdTable;
    private Table sightingTable;

    @Override
    public void createPartControl(Composite parent) {
        TabFolder folder = new TabFolder(parent, SWT.NONE);

        createBirdTab(folder);
        createSightingTab(folder);
    }

    private void createBirdTab(TabFolder folder) {
        TabItem item = new TabItem(folder, SWT.NONE);
        item.setText("Birds");
        
        Composite container = new Composite(folder, SWT.NONE);
        container.setLayout(new GridLayout(1, false));
        item.setControl(container);

        // --- Add Bird Form ---
        Group addGroup = new Group(container, SWT.NONE);
        addGroup.setText("Add New Bird");
        addGroup.setLayout(new GridLayout(2, false));
        addGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        new Label(addGroup, SWT.NONE).setText("Name:");
        Text nameText = new Text(addGroup, SWT.BORDER);
        nameText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        new Label(addGroup, SWT.NONE).setText("Color:");
        Text colorText = new Text(addGroup, SWT.BORDER);
        colorText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        Button submitBtn = new Button(addGroup, SWT.PUSH);
        submitBtn.setText("Save Bird");
        submitBtn.addListener(SWT.Selection, e -> {
            try {
                String json = String.format("{\"name\":\"%s\", \"color\":\"%s\"}", 
                    nameText.getText(), colorText.getText());
                client.postBird(json);
                refreshBirdList();
            } catch (Exception ex) { ex.printStackTrace(); }
        });

        // --- Bird List Table ---
        birdTable = new Table(container, SWT.BORDER | SWT.FULL_SELECTION);
        birdTable.setHeaderVisible(true);
        birdTable.setLayoutData(new GridData(GridData.FILL_BOTH));
        String[] titles = { "ID", "Name", "Color", "Weight", "Height" };
        for (String title : titles) {
            TableColumn col = new TableColumn(birdTable, SWT.NONE);
            col.setText(title);
            col.setWidth(80);
        }
        
        Button refreshBtn = new Button(container, SWT.PUSH);
        refreshBtn.setText("Refresh Birds");
        refreshBtn.addListener(SWT.Selection, e -> refreshBirdList());
    }

    private void createSightingTab(TabFolder folder) {
        TabItem item = new TabItem(folder, SWT.NONE);
        item.setText("Sightings");

        Composite container = new Composite(folder, SWT.NONE);
        container.setLayout(new GridLayout(1, false));
        item.setControl(container);

        // --- Search ---
        Composite searchComp = new Composite(container, SWT.NONE);
        searchComp.setLayout(new GridLayout(3, false));
        new Label(searchComp, SWT.NONE).setText("Search Bird Name:");
        Text searchText = new Text(searchComp, SWT.BORDER);
        Button searchBtn = new Button(searchComp, SWT.PUSH);
        searchBtn.setText("Find Sightings");

        // --- Sighting Table ---
        sightingTable = new Table(container, SWT.BORDER | SWT.FULL_SELECTION);
        sightingTable.setHeaderVisible(true);
        sightingTable.setLayoutData(new GridData(GridData.FILL_BOTH));
        String[] titles = { "Bird", "Location", "Date/Time" };
        for (String title : titles) {
            TableColumn col = new TableColumn(sightingTable, SWT.NONE);
            col.setText(title);
            col.setWidth(120);
        }

        searchBtn.addListener(SWT.Selection, e -> {
            try {
                String results = client.getSightings(searchText.getText());
                System.out.println("Sighting Results: " + results);
                // In a real impl, parse JSON and populate sightingTable
            } catch (Exception ex) { ex.printStackTrace(); }
        });
    }

    private void refreshBirdList() {
        try {
            String birdsJson = client.getBirds();
            System.out.println("Fetched Birds: " + birdsJson);
            // logic to update birdTable goes here
        } catch (Exception ex) { ex.printStackTrace(); }
    }

    @Override
    public void setFocus() {
        birdTable.setFocus();
    }
}