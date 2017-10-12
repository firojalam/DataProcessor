/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package qa.org.qcri.dataprocessor.processors;

/**
 *
 * @author firojalam
 */
public class ClassGroup {
    

    private String grpName = null;
    private String className = null;
    private String source = null;
    private String annotationDesc = null;

    public String getGrpName() {
        return grpName;
    }

    public void setGrpName(String grpName) {
        this.grpName = grpName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getAnnotationDesc() {
        return annotationDesc;
    }

    public void setAnnotationDesc(String annotationDesc) {
        this.annotationDesc = annotationDesc;
    }
    
    

}
