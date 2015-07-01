package edu.uci.isr.archstudio4.comp.xmapper;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Vector;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import edu.uci.isr.archstudio4.graphlayout.gui.IGraphLayoutParameterPanelProvider;

public class xMapperDialog extends Dialog {
	
	private Shell shell = null;
	protected Group changeTypePanel = null;
	protected Label lChangeTypeValue = null;
	protected Group codeGenParametersPanel = null;
	protected Group notificationParametersPanel = null;
	protected int result = 0; // 0: Dialog is canceled; 1: Dialog is OK.

	protected Map<String,String> changeType;
	protected Map<String,String> compDescription;
	protected Map<String,String> archPrescribedClassName;
	protected Map<String,String> userDefinedClassName;
	protected Map<String,Vector> messages;
	
	private boolean isMappingEntireArchitecture = false;
	
	public xMapperDialog(Shell parent, int style) {
		super(parent, style);
		// TODO Auto-generated constructor stub
		changeType = new HashMap<String,String>();
		compDescription = new HashMap<String,String>();
		archPrescribedClassName = new HashMap<String,String>();
		userDefinedClassName = new HashMap<String,String>();
		messages = new HashMap<String,Vector>();
	}

	public xMapperDialog (Shell parent) {
        this (parent, 0); // your default style bits go here (not the Shell's style bits)
	}	
	
	public int open(String[] compChanged){
		
		if (changeType.size()==0){
			isMappingEntireArchitecture = true;
		}
		
		final String[] compIDs = compChanged;
		
        Shell parent = getParent();
        this.shell = new Shell(parent, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		String text = getText();
		if((text == null) || (text.trim().equals(""))){
			text = "Code Mapping Options";
		}
		shell.setText(text);
		shell.setLayout(new FillLayout());

        // Your code goes here (widget creation, set result, etc).
		Composite mainComposite = new Composite(shell, SWT.NONE);
		mainComposite.setLayout(new GridLayout(1, false));

		final Composite cSelectComponent = new Composite(mainComposite, SWT.NONE);
		cSelectComponent.setLayout(new GridLayout(2, false));		
		Label lSelectComponent = new Label(cSelectComponent, SWT.NONE);
		lSelectComponent.setText("Select Component:");		
		String[] compDescriptions = new String[compIDs.length];
		for(int i = 0; i < compDescriptions.length; i++){
			compDescriptions[i] = getCompDescription(compIDs[i]);
			if(compDescriptions[i] == null) compDescriptions[i] = compIDs[i];
		}		
		final Combo cmbSelectComponent = new Combo(cSelectComponent, SWT.READ_ONLY);
		cmbSelectComponent.setItems(compDescriptions);
		cmbSelectComponent.select(0);

		cmbSelectComponent.addSelectionListener(new SelectionListener(){
			public void widgetSelected(SelectionEvent e){
				int selectionIndex = cmbSelectComponent.getSelectionIndex();
				if (isMappingEntireArchitecture){
					setupCodeGenParametersPanel(compIDs[selectionIndex]);					
				} else {
					if (lChangeTypeValue != null){
						lChangeTypeValue.setText(getChangeType(compIDs[selectionIndex])+".");										
					}
					setupCodeGenParametersPanel(compIDs[selectionIndex]);
					setupNotificationParametersPanel(compIDs[selectionIndex]);					
				}
			}
			public void widgetDefaultSelected(SelectionEvent e){
				widgetSelected(e);
			}
		});

		if (!isMappingEntireArchitecture){
			changeTypePanel = new Group(mainComposite, SWT.NONE);
			changeTypePanel.setText("Change Description");
			changeTypePanel.setLayout(new GridLayout(2, false));
			changeTypePanel.setLayoutData(new GridData(265, SWT.DEFAULT));
			lChangeTypeValue = new Label(changeTypePanel, SWT.NONE);
			lChangeTypeValue.setText(getChangeType(compIDs[0])+".");				
		}

		codeGenParametersPanel = new Group(mainComposite, SWT.NONE);
		codeGenParametersPanel.setText("Code Generation");
		codeGenParametersPanel.setLayout(new GridLayout(1, false));
		setupCodeGenParametersPanel(compIDs[0]);

		if (!isMappingEntireArchitecture){
			notificationParametersPanel = new Group(mainComposite, SWT.NONE);
			notificationParametersPanel.setText("Change Notification");
			notificationParametersPanel.setLayout(new GridLayout(1, false));
			setupNotificationParametersPanel(compIDs[0]);			
		}
		
		Composite cButtons = new Composite(mainComposite, SWT.NONE);
		cButtons.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false));
		cButtons.setLayout(new GridLayout(2, false));

		Button bOK = new Button(cButtons, SWT.PUSH);
		bOK.setText("OK");
		bOK.addSelectionListener(new SelectionListener(){
			public void widgetSelected(SelectionEvent e){
				done(1);
				return;
			}
			
			public void widgetDefaultSelected(SelectionEvent e){
				widgetSelected(e);
			}
		});
		
		Button bCancel = new Button(cButtons, SWT.PUSH);
		bCancel.setText("Cancel");
		bCancel.addSelectionListener(new SelectionListener(){
			public void widgetSelected(SelectionEvent e){
				done(0);
				return;
			}
			
			public void widgetDefaultSelected(SelectionEvent e){
				widgetSelected(e);
			}
		});
		
		shell.pack();
		shell.open();
        Display display = parent.getDisplay();
        while (!shell.isDisposed()) {
                if (!display.readAndDispatch()) display.sleep();
        }
        return result;
    }

	public void setupCodeGenParametersPanel(String compID){
		
		final String ID = compID;
		
		//dispose old controls
		Control[] children = codeGenParametersPanel.getChildren();
		for(int i = 0; i < children.length; i++){
			children[i].dispose();
		}
		
		Label lArch = new Label(codeGenParametersPanel, SWT.NONE);
		lArch.setText("Architecture-Prescribed Class Name:");		
		final Text tArchClassName = new Text(codeGenParametersPanel, SWT.BORDER);
		tArchClassName.setLayoutData(new GridData(250, SWT.DEFAULT));
		String className = getArchPrescribedClassName(compID);
		if (className == null){
			className = "";
		}
		tArchClassName.setText(className);
		tArchClassName.addModifyListener(new ModifyListener(){
			public void modifyText(ModifyEvent e) {
				// TODO Auto-generated method stub
				setArchPrescribedClassName(ID, tArchClassName.getText());
			}			
		});

		Label lImp = new Label(codeGenParametersPanel, SWT.NONE);
		lImp.setText("User-Defined Class Name:");		
		final Text tImpClassName = new Text(codeGenParametersPanel, SWT.BORDER);
		tImpClassName.setLayoutData(new GridData(250, SWT.DEFAULT));
		className = getUserDefinedClassName(compID);
		if (className == null){
			className = "";
		}
		tImpClassName.setText(className);						
		tImpClassName.addModifyListener(new ModifyListener(){
			public void modifyText(ModifyEvent e) {
				// TODO Auto-generated method stub
				setUserDefinedClassName(ID, tImpClassName.getText());
			}			
		});
		
		shell.layout(true, true);
		shell.pack(true);		
	}
	
	public void setupNotificationParametersPanel(String compID){
		
		final String ID = compID;
		
		//dispose old controls
		Control[] children = notificationParametersPanel.getChildren();
		for(int i = 0; i < children.length; i++){
			children[i].dispose();
		}
		
		Label lTarget = new Label(notificationParametersPanel, SWT.NONE);
		lTarget.setText("Class To Be Notified:");		
		final Text targetClassName = new Text(notificationParametersPanel, SWT.BORDER);
		targetClassName.setLayoutData(new GridData(250, SWT.DEFAULT));
		String className = getUserDefinedClassName(compID);
		if (className == null){
			className = "";
		}
		targetClassName.setText(className);
		targetClassName.setEditable(false);
		
		Label lMessages = new Label(notificationParametersPanel, SWT.NONE);
		lMessages.setText("Messages:");		
		final Text msg = new Text(notificationParametersPanel, SWT.MULTI | SWT.WRAP);
		msg.setLayoutData(new GridData(250, 100));
		Vector m = getMessages(compID);
		if (m!=null){
			for (Enumeration e = m.elements() ; e.hasMoreElements() ;) {
				msg.append((String)e.nextElement());
				msg.append(Text.DELIMITER);
			}					
		}
		msg.addModifyListener(new ModifyListener(){
			public void modifyText(ModifyEvent e) {
				// TODO Auto-generated method stub
				StringTokenizer st = new StringTokenizer(msg.getText(),Text.DELIMITER+"\n");
				Vector<String> v = new Vector<String>();
				while (st.hasMoreTokens()) {
					String txt = st.nextToken();
					if ((txt!=null)&&(txt.length()>0)){
						v.add(txt);
					}
				}
				setMessages(ID, v);
			}			
		});

		shell.layout(true, true);
		shell.pack(true);		
	}

	protected void done(int r){
		this.shell.dispose();
		result = r;
	}
	
	public void setMessages(String compId, Vector m){
		messages.put(compId, m);
	}
	
	public Vector getMessages(String compId){
		return messages.get(compId);
	}

	public void setChangeType(String compId, String apcn){
		changeType.put(compId, apcn);
	}
	
	public String getChangeType(String compId){
		return changeType.get(compId);
	}

	public void setCompDescription(String compId, String apcn){
		compDescription.put(compId, apcn);
	}
	
	public String getCompDescription(String compId){
		return compDescription.get(compId);
	}
	
	public void setArchPrescribedClassName(String compId, String apcn){
		archPrescribedClassName.put(compId, apcn);
	}
	
	public String getArchPrescribedClassName(String compId){
		return archPrescribedClassName.get(compId);
	}

	public void setUserDefinedClassName(String compId, String udcn){
		userDefinedClassName.put(compId, udcn);
	}
	
	public String getUserDefinedClassName(String compId){
		return userDefinedClassName.get(compId);
	}
}
