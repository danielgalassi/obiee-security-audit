<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="xml" media-type="text/plain"/>
	<xsl:template match="/">
		<WebCatalog>
			<xsl:for-each select="/WebCat/ApplicationRoleList">
				<xsl:for-each select="./ApplicationRole">
					<xsl:variable name="AppRole" select="@ApplicationRoleName"/>
					<xsl:copy>
						<xsl:attribute name="isAdmin">
							<xsl:value-of select="count(../..//ComponentList/Component[@ComponentName = 'Admin System Privs']/Privilege/RoleList/Role[@access='Granted'][.=$AppRole]) > 0"/>
						</xsl:attribute>
						<xsl:attribute name="isSuperUser">
							<xsl:value-of select="count(../..//ComponentList/Component[@ComponentName = 'generalprivs']/Privilege[@PrivilegeName='Global Answers']/RoleList/Role[@access='Granted'][.=$AppRole]) > 0"/>
						</xsl:attribute>
						<xsl:attribute name="isServiceDesk">
							<xsl:value-of select="contains($AppRole, 'Service Desk')"/>
						</xsl:attribute>
						<xsl:attribute name="isEIM">
							<xsl:value-of select="contains($AppRole, 'NBN EIM')"/>
						</xsl:attribute>
						<xsl:value-of select="$AppRole"/>
					</xsl:copy>
				</xsl:for-each>
			</xsl:for-each>
		</WebCatalog>
	</xsl:template>
</xsl:stylesheet>