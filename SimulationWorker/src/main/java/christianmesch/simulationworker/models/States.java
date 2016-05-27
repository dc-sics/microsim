/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package christianmesch.simulationworker.models;

import java.io.Serializable;
import java.util.Objects;

/**
 * State class for a person
 * @author Christian Mesch
 */
public class States implements Serializable, Comparable<States> {
	
	/**
	 * The different health stages which can be used.
	 * Please note that NONE is only to be used when creating a filter.
	 */
	public static enum Stage {
		HEALTHY,
		LOCALISED,
		METASTATIC,
		NONE
	}
	
	/**
	 * The different diagnosis states which can be used.
	 * Please note that NONE is only to be used when creating a filter.
	 */
	public static enum Diagnosis {
		NOT_DIAGNOSED,
		CLINICAL_DIAGNOSIS,
		SCREEN_DIAGNOSIS,
		NONE
	}
	
	public static enum ExtGrade {
		GLEASON_LE_6,
		GLEASON_7,
		GLEASON_GE_8,
		NONE
	}
	
	public static enum ScreenType {
		NO_SCREENING,
		RANDOM_SCREEN_50_TO_70,
		TWO_YEARLY_SCREEN_50_TO_70,
		FOUR_YEARLY_SCREEN_50_TO_70,
		SCREEN_50,
		SCREEN_60,
		SCREEN_70,
		SCREEN_UPTAKE,
		STOCKHOLM3_GOTEBORG,
		STOCKHOLM3_RISK_STRATIFIED,
		GOTEBORG,
		RIST_STRATIFIED,
		MIXED_SCREENING,
		REGULAR_SCREEN,
		SINGLE_SCREEN,
		NONE
	}
	
	public static enum TreatmentType {
		NO_TREATMENT,
		CM,
		RP,
		RT,
		NONE
	}
	
	public static enum Survival {
		STAGE_SHIFT_BASED,
		LEAD_TIME_BASED,
		NONE
	}
	
	public static enum BiomarkerModel {
		RANDOM_CORRECTION,
		PSA_INFORMED_CORRECTION,
		NONE
	}
	
	private Stage stage;
	private Diagnosis diagnosis;
	private ExtGrade extGrade;
	private ScreenType screenType;
	private TreatmentType treatmentType;
	private Survival survival;
	private BiomarkerModel biomarkerModel;
	
	/**
	 * Standard constructor.
	 * Initializes the object with starting values.
	 */
	public States() {
		stage = Stage.HEALTHY;
		diagnosis = Diagnosis.NOT_DIAGNOSED;
		screenType = ScreenType.NO_SCREENING;
		treatmentType = TreatmentType.NO_TREATMENT;
		extGrade = ExtGrade.NONE;
		survival = Survival.NONE;
		biomarkerModel = BiomarkerModel.NONE;
	}
	
	/**
	 * Copy constructor.
	 * Copies the internals of the original object
	 * @param original States to be copied
	 */
	public States(States original) {
		stage = original.stage;
		diagnosis = original.diagnosis;
		extGrade = original.extGrade;
		screenType = original.screenType;
		treatmentType = original.treatmentType;
		survival = original.survival;
		biomarkerModel = original.biomarkerModel;
		
	}

	/**
	 * Constructor which takes all states as parameters
	 * @param stage Health stage
	 * @param diagnosis  Diagnosis
	 */
	public States(Stage stage, Diagnosis diagnosis) {
		this.stage = stage;
		this.diagnosis = diagnosis;
	}
	
	public Stage getStage() {
		return stage;
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}

	public Diagnosis getDiagnosis() {
		return diagnosis;
	}

	public void setDiagnosis(Diagnosis diagnosis) {
		this.diagnosis = diagnosis;
	}

	public ExtGrade getExtGrade() {
		return extGrade;
	}

	public void setExtGrade(ExtGrade extGrade) {
		this.extGrade = extGrade;
	}

	public ScreenType getScreenType() {
		return screenType;
	}

	public void setScreenType(ScreenType screenType) {
		this.screenType = screenType;
	}

	public TreatmentType getTreatmentType() {
		return treatmentType;
	}

	public void setTreatmentType(TreatmentType treatmentType) {
		this.treatmentType = treatmentType;
	}

	public Survival getSurvival() {
		return survival;
	}

	public void setSurvival(Survival survival) {
		this.survival = survival;
	}

	public BiomarkerModel getBiomarkerModel() {
		return biomarkerModel;
	}

	public void setBiomarkerModel(BiomarkerModel biomarkerModel) {
		this.biomarkerModel = biomarkerModel;
	}
	
	/**
	 * Filter out states with the possibility to have "wildcards".
	 * Compare this to multiple other States.
	 * @param other
	 * @return 
	 */
	public boolean filter(States other) {
		// Is states irrelevant? I.e. we want to have all combinations of states
		if(this.diagnosis.equals(Diagnosis.NONE) && 
				this.stage.equals(Stage.NONE))
			return true;
		
		// Is diagnosis a wildcard?
		if(this.diagnosis.equals(Diagnosis.NONE))
			return this.stage.equals(other.stage);
		
		// Is stage a wildcard?
		if(this.stage.equals(Stage.NONE))
			return this.diagnosis.equals(other.diagnosis);
		
		// No wildcards, compare everything
		return this.diagnosis.equals(other.diagnosis) &&
				this.stage.equals(other.stage);
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 59 * hash + Objects.hashCode(this.stage);
		hash = 59 * hash + Objects.hashCode(this.diagnosis);
		hash = 59 * hash + Objects.hashCode(this.extGrade);
		hash = 59 * hash + Objects.hashCode(this.screenType);
		hash = 59 * hash + Objects.hashCode(this.treatmentType);
		hash = 59 * hash + Objects.hashCode(this.survival);
		hash = 59 * hash + Objects.hashCode(this.biomarkerModel);
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if(this == obj) {
			return true;
		}
		if(obj == null) {
			return false;
		}
		if(getClass() != obj.getClass()) {
			return false;
		}
		final States other = (States) obj;
		if(this.stage != other.stage) {
			return false;
		}
		if(this.diagnosis != other.diagnosis) {
			return false;
		}
		if(this.extGrade != other.extGrade) {
			return false;
		}
		if(this.screenType != other.screenType) {
			return false;
		}
		if(this.treatmentType != other.treatmentType) {
			return false;
		}
		if(this.survival != other.survival) {
			return false;
		}
		if(this.biomarkerModel != other.biomarkerModel) {
			return false;
		}
		return true;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		
		builder.append("{ stage = ")
				.append(stage)
				.append(", diagnosis = ")
				.append(diagnosis)
				.append(", extGrade = ")
				.append(extGrade)
				.append(", screenType = ")
				.append(screenType)
				.append(", treatmentType = ")
				.append(treatmentType)
				.append(", survival = ")
				.append(survival)
				.append(", biomarkerModel = ")
				.append(biomarkerModel)
				.append(" }");
		
		return builder.toString();
	}
	
	@Override
	public int compareTo(States o) {
		int health = stage.compareTo(o.stage);
		if(health != 0) return health;
		
		int diag = diagnosis.compareTo(o.diagnosis);
		if(diag != 0) return diag;
		
		int ext = extGrade.compareTo(o.extGrade);
		if(ext != 0) return ext;
		
		int scr = screenType.compareTo(o.screenType);
		if(scr != 0) return scr;
		
		int trea = treatmentType.compareTo(o.treatmentType);
		if(trea != 0) return trea;
		
		int surv = survival.compareTo(o.survival);
		if(surv != 0) return surv;
		
		int bio = biomarkerModel.compareTo(o.biomarkerModel);
		
		return bio;
	}

	
}
