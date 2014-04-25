<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="xml" media-type="text/plain"/>
	<xsl:template match="/">
		<WebCatalog>
			<RoleTypeMasterList>
				<RoleType Name="Administrators"/>
				<RoleType Name="Service Desk"/>
				<RoleType Name="EIM"/>
				<RoleType Name="SuperUsers"/>
				<RoleType Name="Business Users"/>
			</RoleTypeMasterList>
			<xsl:for-each select="/WebCat">
				<ApplicationRoleList>
					<xsl:for-each select="./ApplicationRoleList/ApplicationRole">
						<xsl:variable name="myRole" select="@ApplicationRoleName"/>
						<xsl:copy>
							<xsl:copy-of select="@ApplicationRoleName"/>
							<xsl:attribute name="RoleType">
								<xsl:choose>
									<xsl:when test="count(../..//ComponentList/Component[@ComponentName = 'Admin System Privs']/Privilege/RoleList/Role[@access='Granted'][.=$myRole and not(contains($myRole, 'Service Desk')) and not(contains($myRole, 'NBN EIM')) and not(contains($myRole, 'Session_Log_Read')) and not(contains($myRole, 'BISystem')) and not(contains($myRole, 'AuthenticatedUser'))]) > 0">Administrators</xsl:when>
									<xsl:when test="count(../..//ComponentList/Component[@ComponentName = 'generalprivs']/Privilege[@PrivilegeName='Global Answers']/RoleList/Role[@access='Granted'][.=$myRole and not(contains($myRole, 'Service Desk')) and not(contains($myRole, 'NBN EIM')) and not(contains($myRole, 'Session_Log_Read')) and not(contains($myRole, 'BISystem')) and not(contains($myRole, 'AuthenticatedUser'))]) > 0">SuperUsers</xsl:when>
									<xsl:when test="contains($myRole, 'NBN EIM') or contains($myRole, 'Session_Log_Read')">EIM</xsl:when>
									<xsl:when test="contains($myRole, 'Service Desk')">Service Desk</xsl:when>
									<xsl:when test="contains($myRole, 'BISystem') or  contains($myRole, 'AuthenticatedUser')">System User</xsl:when>
									<xsl:otherwise>Business Users</xsl:otherwise>
								</xsl:choose>
							</xsl:attribute>
						</xsl:copy>
					</xsl:for-each>
				</ApplicationRoleList>
				<xsl:for-each select="./ComponentList">
					<ComponentList>
						<xsl:for-each select="./Component">
							<Component>
								<xsl:copy-of select="@ComponentName"/>
								<xsl:for-each select="./Privilege">
									<Privilege>
										<xsl:copy-of select="@PrivilegeName"/>
										<xsl:for-each select="./RoleList">
											<RoleList>
												<xsl:for-each select="./Role">
													<Role>
														<xsl:copy-of select="@access"/>
														<xsl:attribute name="RoleType">
														<!-- calculate the role type here -->
															<xsl:variable name="myRole" select="."></xsl:variable>
															<xsl:choose>
																<xsl:when test="count(../../../../..//ComponentList/Component[@ComponentName = 'Admin System Privs']/Privilege/RoleList/Role[@access='Granted'][.=$myRole and not(contains($myRole, 'Service Desk')) and not(contains($myRole, 'NBN EIM')) and not(contains($myRole, 'Session_Log_Read')) and not(contains($myRole, 'BISystem')) and not(contains($myRole, 'AuthenticatedUser'))]) > 0">Administrators</xsl:when>
																<xsl:when test="count(../../../../..//ComponentList/Component[@ComponentName = 'generalprivs']/Privilege[@PrivilegeName='Global Answers']/RoleList/Role[@access='Granted'][.=$myRole and not(contains($myRole, 'Service Desk')) and not(contains($myRole, 'NBN EIM')) and not(contains($myRole, 'Session_Log_Read')) and not(contains($myRole, 'BISystem')) and not(contains($myRole, 'AuthenticatedUser'))]) > 0">SuperUsers</xsl:when>
																<xsl:when test="contains($myRole, 'NBN EIM') or contains($myRole, 'Session_Log_Read')">EIM</xsl:when>
																<xsl:when test="contains($myRole, 'Service Desk')">Service Desk</xsl:when>
																<xsl:when test="contains($myRole, 'BISystem') or  contains($myRole, 'AuthenticatedUser')">System User</xsl:when>
																<xsl:otherwise>Business Users</xsl:otherwise>
															</xsl:choose>
														</xsl:attribute>
														<xsl:value-of select="."/>
													</Role>
												</xsl:for-each>
											</RoleList>
										</xsl:for-each>
									</Privilege>
								</xsl:for-each>
							</Component>
						</xsl:for-each>
					</ComponentList>
				</xsl:for-each>
			</xsl:for-each>
		</WebCatalog>
	</xsl:template>
</xsl:stylesheet>
