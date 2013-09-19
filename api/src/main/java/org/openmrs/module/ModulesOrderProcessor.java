package org.openmrs.module;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.NotImplementedException;

public class ModulesOrderProcessor {
	
	private Set<ModuleDependency> moduleDependency = new HashSet<ModuleDependency>();
	
	/**
	 * Add dependency : firstModule need to be started before secondModule
	 * @param firstModule need to be started before secondModule
	 * @param secondModule need to be started after firstModule
	 */
	public void addBeforeStartDependency(Module firstModule, Module secondModule){
		moduleDependency.add(new ModuleDependency(secondModule, firstModule));
	}
	
	/**
	 * Add dependency: firstModule required secondModule
	 * @param firstModule required secondModule
	 * @param secondModule is needed by firstModule
	 */
	public void addRequiredDependency(Module firstModule, Module secondModule){
		moduleDependency.add(new ModuleDependency(firstModule, secondModule));
	}
	
	
	public List<Module> getLoadingOrder(){
		throw new NotImplementedException();
	}
	
	
	private class ModuleDependency{
		
		private Module module;
		private Module dependsOnModule;
		
		public ModuleDependency(Module module, Module dependsOnModule) {
			this.module = module;
			this.dependsOnModule = dependsOnModule;
		}

		public Module getModule() {
			return module;
		}

		public Module getDependsOnModule() {
			return dependsOnModule;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime
					* result
					+ ((dependsOnModule == null) ? 0 : dependsOnModule
							.hashCode());
			result = prime * result
					+ ((module == null) ? 0 : module.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			ModuleDependency other = (ModuleDependency) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (dependsOnModule == null) {
				if (other.dependsOnModule != null)
					return false;
			} else if (!dependsOnModule.equals(other.dependsOnModule))
				return false;
			if (module == null) {
				if (other.module != null)
					return false;
			} else if (!module.equals(other.module))
				return false;
			return true;
		}

		private ModulesOrderProcessor getOuterType() {
			return ModulesOrderProcessor.this;
		}
		
		
	}
}
