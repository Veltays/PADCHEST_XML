<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <!-- === TEMPLATE PRINCIPAL === -->
    <xsl:template match="/">

        <html>
            <head>
                <title>PadChest – Images</title>
                <style>
                    body { font-family: Arial, sans-serif; background: #f3f3f3; padding: 20px; }
                    table { border-collapse: collapse; width: 100%; background: white; }
                    th { background: #444; color: white; padding: 10px; }
                    td { border: 1px solid #ccc; padding: 8px; }
                    tr:nth-child(even) { background: #f0f0f0; }
                    h1 { margin-bottom: 20px; }
                </style>
            </head>

            <body>
                <h1>Liste des 20 premières images PadChest</h1>

                <table>
                    <tr>
                        <th>ID</th>
                        <th>ImageID</th>
                        <th>ImageDir</th>
                        <th>StudyID</th>
                        <th>PatientID</th>
                        <th>PatientBirth</th>
                        <th>Projection</th>
                        <th>MethodProjection</th>
                        <th>Pediatric</th>
                        <th>ReportID</th>
                        <th>Report</th>
                        <th>MethodLabel</th>
                        <th>Labels</th>
                        <th>Localizations</th>
                        <th>LabelsLocalizationsBySentence</th>
                        <th>labelCUIS</th>
                        <th>LocalizationsCUIS</th>
                    </tr>

                    <!-- 🔥 LIMITATION À 20 IMAGES -->
                    <xsl:for-each select="Images/image[position() &lt;= 20]">

                        <tr>
                            <td><xsl:value-of select="@Identifiant"/></td>
                            <td><xsl:value-of select="ImageID"/></td>
                            <td><xsl:value-of select="ImageDir"/></td>
                            <td><xsl:value-of select="StudyID"/></td>
                            <td><xsl:value-of select="PatientID"/></td>
                            <td><xsl:value-of select="PatientBirth"/></td>
                            <td><xsl:value-of select="Projection"/></td>
                            <td><xsl:value-of select="MethodProjection"/></td>
                            <td><xsl:value-of select="Pediatric"/></td>
                            <td><xsl:value-of select="ReportID"/></td>
                            <td><xsl:value-of select="Report"/></td>
                            <td><xsl:value-of select="MethodLabel"/></td>

                            <!-- Labels -->
                            <td>
                                <xsl:for-each select="Labels/Label">
                                    <xsl:value-of select="."/>
                                    <xsl:if test="position() != last()">, </xsl:if>
                                </xsl:for-each>
                            </td>

                            <!-- Localizations -->
                            <td>
                                <xsl:for-each select="Localizations/Localization">
                                    <xsl:value-of select="."/>
                                    <xsl:if test="position() != last()">, </xsl:if>
                                </xsl:for-each>
                            </td>

                            <!-- LabelsLocalizationsBySentence -->
                            <td>
                                <xsl:for-each select="LabelsLocalizationsBySentence/Sentence">
                                    [
                                    <xsl:for-each select="LabelSentence">
                                        <xsl:value-of select="."/>
                                        <xsl:if test="position() != last()">, </xsl:if>
                                    </xsl:for-each>
                                    ]
                                    <xsl:if test="position() != last()"> ; </xsl:if>
                                </xsl:for-each>
                            </td>

                            <!-- labelCUIS -->
                            <td>
                                <xsl:for-each select="labelCUIS/labelCUI">
                                    <xsl:value-of select="."/>
                                    <xsl:if test="position() != last()">, </xsl:if>
                                </xsl:for-each>
                            </td>

                            <!-- LocalizationsCUIS -->
                            <td>
                                <xsl:for-each select="LocalizationsCUIS/LocalizationsCUI">
                                    <xsl:value-of select="."/>
                                    <xsl:if test="position() != last()">, </xsl:if>
                                </xsl:for-each>
                            </td>
                        </tr>

                    </xsl:for-each>

                </table>

            </body>
        </html>

    </xsl:template>
</xsl:stylesheet>