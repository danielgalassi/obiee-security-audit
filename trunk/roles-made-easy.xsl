<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="xml" media-type="text/plain"/>
	<xsl:template match="/">
		<WebCatalog>
			<xsl:for-each select="./WebCatalog">
				<xsl:for-each select="./RoleTypeMasterList">
					<RoleTypeMasterList>
						<xsl:for-each select=".">
							<xsl:copy-of select="*"/>
						</xsl:for-each>
					</RoleTypeMasterList>
				</xsl:for-each>
				<xsl:for-each select="./ApplicationRoleList">
					<ApplicationRoleList>
						<xsl:for-each select=".">
							<xsl:copy-of select="*"/>
						</xsl:for-each>
					</ApplicationRoleList>
				</xsl:for-each>
				<xsl:for-each select="./ComponentList">
					<ComponentList>
						<xsl:for-each select="./Component">
							<Component>
								<xsl:attribute name="ComponentName">
									<xsl:value-of select="@ComponentName"/>
								</xsl:attribute>
								<xsl:for-each select="./Privilege">
									<Privilege>
										<xsl:attribute name="PrivilegeName">
											<xsl:value-of select="@PrivilegeName"/>
										</xsl:attribute>
										<xsl:for-each select="./RoleList">
											<RoleList>
												<xsl:for-each select="./Role">
													<Role>
														<xsl:attribute name="access">
															<xsl:value-of select="@access"/>
														</xsl:attribute>
														<xsl:attribute name="RoleType">
											<!-- calculate the role type here: ADM, SUP, EIM, SU, BIZ -->
															<xsl:variable name="myRole" select="."></xsl:variable>
															<xsl:value-of select="count(../../../../..//ApplicationRole[@isAdmin='true'][.=$myRole])"/>
															<xsl:value-of select="count(../../../../..//ApplicationRole[@isServiceDesk='true'][.=$myRole])"/>
															<xsl:value-of select="count(../../../../..//ApplicationRole[@isEIM='true'][.=$myRole])"/>
															<xsl:value-of select="count(../../../../..//ApplicationRole[@isSuperUser='true'][.=$myRole])"/>
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
