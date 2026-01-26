package com.example.bird.ui;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.part.ViewPart;
import com.example.bird.client.BirdServiceClient;

/**
 * Eclipse ViewPart providing the UI for Bird and Sighting management.
 * Uses Jobs for thread-safe API communication to keep UI responsive.
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

        Group addGroup = new Group(container, SWT.NONE);
        addGroup.setText("Register New Bird Species");
        addGroup.setLayout(new GridLayout(4, false));
        addGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        new Label(addGroup, SWT.NONE).setText("Name:");
        Text nameTxt = new Text(addGroup, SWT.BORDER);
        new Label(addGroup, SWT.NONE).setText("Color:");
        Text colorTxt = new Text(addGroup, SWT.BORDER);
        new Label(addGroup, SWT.NONE).setText("Weight (g):");
        Text weightTxt = new Text(addGroup, SWT.BORDER);
        new Label(addGroup, SWT.NONE).setText("Height (cm):");
        Text heightTxt = new Text(addGroup, SWT.BORDER);

        Button saveBtn = new Button(addGroup, SWT.PUSH);
        saveBtn.setText("Save Bird");
        saveBtn.addListener(SWT.Selection, e -> {
            String json = String.format("{\"name\":\"%s\",\"color\":\"%s\",\"weight\":%s,\"height\":%s}",
                nameTxt.getText(), colorTxt.getText(), weightTxt.getText(), heightTxt.getText());
            runApiJob("Saving Bird", () -> {
                client.postBird(json);
                refreshBirds();
            });
        });

        birdTable = new Table(container, SWT.BORDER | SWT.FULL_SELECTION);
        birdTable.setHeaderVisible(true);
        birdTable.setLayoutData(new GridData(GridData.FILL_BOTH));
        String[] titles = { "ID", "Name", "Color", "Weight", "Height" };
        for (String t : titles) { new TableColumn(birdTable, SWT.NONE).setText(t); birdTable.getColumn(birdTable.getColumnCount()-1).setWidth(100); }

        Button refreshBtn = new Button(container, SWT.PUSH);
        refreshBtn.setText("Refresh List");
        refreshBtn.addListener(SWT.Selection, e -> refreshBirds());
    }

    private void createSightingTab(TabFolder folder) {
        TabItem item = new TabItem(folder, SWT.NONE);
        item.setText("Sightings");
        Composite container = new Composite(folder, SWT.NONE);
        container.setLayout(new GridLayout(1, false));
        item.setControl(container);

        Group addSightingGrp = new Group(container, SWT.NONE);
        addSightingGrp.setText("Record Sighting");
        addSightingGrp.setLayout(new GridLayout(4, false));
        addSightingGrp.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        new Label(addSightingGrp, SWT.NONE).setText("Bird ID:");
        Text bIdTxt = new Text(addSightingGrp, SWT.BORDER);
        new Label(addSightingGrp, SWT.NONE).setText("Location:");
        Text locTxt = new Text(addSightingGrp, SWT.BORDER);
        new Label(addSightingGrp, SWT.NONE).setText("Date/Time (ISO):");
        Text dtTxt = new Text(addSightingGrp, SWT.BORDER);
        dtTxt.setMessage("2023-10-27T10:00:00");

        Button addBtn = new Button(addSightingGrp, SWT.PUSH);
        addBtn.setText("Add Sighting");
        addBtn.addListener(SWT.Selection, e -> {
            String json = String.format("{\"bird\":{\"id\":%s},\"location\":\"%s\",\"dateTime\":\"%s\"}",
                bIdTxt.getText(), locTxt.getText(), dtTxt.getText());
            runApiJob("Saving Sighting", () -> client.postSighting(json));
        });

        Composite searchComp = new Composite(container, SWT.NONE);
        searchComp.setLayout(new GridLayout(3, false));
        new Label(searchComp, SWT.NONE).setText("Search Bird Name:");
        Text searchTxt = new Text(searchComp, SWT.BORDER);
        Button searchBtn = new Button(searchComp, SWT.PUSH);
        searchBtn.setText("Search");

        sightingTable = new Table(container, SWT.BORDER | SWT.FULL_SELECTION);
        sightingTable.setHeaderVisible(true);
        sightingTable.setLayoutData(new GridData(GridData.FILL_BOTH));
        String[] titles = { "Bird", "Location", "Time" };
        for (String t : titles) { new TableColumn(sightingTable, SWT.NONE).setText(t); sightingTable.getColumn(sightingTable.getColumnCount()-1).setWidth(150); }

        searchBtn.addListener(SWT.Selection, e -> {
            runApiJob("Searching Sightings", () -> {
                String results = client.getSightings(searchTxt.getText());
                System.out.println("Results: " + results); 
                // Actual JSON parsing would happen here
            });
        });
    }

    private void refreshBirds() {
        runApiJob("Fetching Birds", () -> {
            String json = client.getBirds();
            Display.getDefault().asyncExec(() -> {
                if (!birdTable.isDisposed()) {
                    birdTable.removeAll();
                    // In a real app, use a JSON parser like Jackson/Gson to populate rows
                    TableItem itm = new TableItem(birdTable, SWT.NONE);
                    itm.setText(new String[]{"LOG", "Check console for JSON", "", "", ""});
                }
            });
        });
    }

    private void runApiJob(String name, ApiRunnable runnable) {
        Job job = new Job(name) {
            @Override
            protected IStatus run(IProgressMonitor monitor) {
                try {
                    runnable.run();
                    return Status.OK_STATUS;
                } catch (Exception ex) {
                    return new Status(IStatus.ERROR, "com.example.bird.ui", "API Error: " + ex.getMessage());
                }
            }
        };
        job.setUser(true);
        job.schedule();
    }

    @FunctionalInterface interface ApiRunnable { void run() throws Exception; }
    @Override public void setFocus() { birdTable.setFocus(); }
}