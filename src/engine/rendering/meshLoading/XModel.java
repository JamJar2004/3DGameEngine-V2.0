package engine.rendering.meshLoading;

import engine.core.Util;
import engine.core.math.Vector2f;
import engine.core.math.Vector3f;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class XModel
{
    private ArrayList<Vector3f> positions;
    private ArrayList<Vector3f> colors;
    private ArrayList<Integer>  indices;

    public XModel(String fileName)
    {
        positions = new ArrayList<>();
        colors    = new ArrayList<>();
        indices   = new ArrayList<>();

        BufferedReader meshReader = null;
        String line;
        try
        {
            meshReader = new BufferedReader(new FileReader(fileName));
            boolean enteredRoot = false;

            boolean verticesStarted = false;
            boolean indicesStarted  = false;
            boolean materialListStarted = false;
            boolean materialStarted = false;

            int vertexCount = 0;
            Vector3f currColor = new Vector3f(1, 1, 1);

            while((line = meshReader.readLine()) != null)
            {
                line = line.replace("\t", "");
                line = line.replace("\n", "");

                if(line.startsWith("Frame Root"))
                {
                    enteredRoot = true;
                }
                else if(!enteredRoot)
                    continue;

                if(enteredRoot)
                {
                    if(line.endsWith(";") || line.endsWith(",") || line.endsWith("{") || line.endsWith("}"))
                    {
                        String[] tokens = line.split(";");
                        tokens = Util.removeEmptyStrings(tokens);
                        if(tokens.length < 2 && line.endsWith(";"))
                        {
                            if(!verticesStarted)
                                verticesStarted = true;
                            else
                            {
                                verticesStarted = false;
                                indicesStarted  = true;
                            }
                        }
                        else if(materialStarted && tokens.length == 4)
                        {
                            tokens[0] = tokens[0].replace(",", "");
                            tokens[1] = tokens[1].replace(",", "");
                            tokens[2] = tokens[2].replace(",", "");
                            currColor = new Vector3f(Float.parseFloat(tokens[0]),
                                                     Float.parseFloat(tokens[1]),
                                                     Float.parseFloat(tokens[2]));

                            for(int i = vertexCount; i < positions.size(); i++)
                            {
                                colors.add(currColor);
                            }
                        }
                        else
                        {
                            if(verticesStarted && tokens.length > 2 && !materialListStarted)
                            {
                                tokens[0] = tokens[0].replace(",", "");
                                tokens[1] = tokens[1].replace(",", "");
                                tokens[2] = tokens[2].replace(",", "");
                                positions.add(new Vector3f(Float.parseFloat(tokens[0]),
                                                           Float.parseFloat(tokens[1]),
                                                           Float.parseFloat(tokens[2])));
                            }
                            else if(indicesStarted)
                            {
                                if(line.startsWith("MeshMaterialList"))
                                {
                                    verticesStarted = false;
                                    indicesStarted = false;
                                    materialListStarted = true;
                                }
                                else if(line.startsWith("Material"))
                                {
                                    materialStarted     = true;
                                }
                                else if(line.endsWith("}"))
                                {
                                    materialStarted     = false;
                                    materialListStarted = false;
                                    vertexCount = positions.size();
                                }
                                else
                                {
                                    if(tokens.length > 1)
                                    {
                                        String[] indexTokens = tokens[1].split(",");
                                        indexTokens = Util.removeEmptyStrings(indexTokens);
                                        indexTokens = Util.removeWhiteSpaces(indexTokens);
                                        if(indexTokens.length >= 3)
                                        {
                                            for (int i = 0; i <= indexTokens.length - 3; i++)
                                            {
                                                indices.add(Integer.parseInt(indexTokens[0    ]) + vertexCount);
                                                indices.add(Integer.parseInt(indexTokens[1 + i]) + vertexCount);
                                                indices.add(Integer.parseInt(indexTokens[2 + i]) + vertexCount);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.exit(1);
        }
        //System.out.println("Finished loading!");
    }

    public ArrayList<Vector3f> getPositions()
    {
        return positions;
    }

    public ArrayList<Vector3f> getColors()
    {
        return colors;
    }

    public ArrayList<Integer>  getIndices()
    {
        return indices;
    }

    public IndexedModel toIndexedModel()
    {
        IndexedModel result = new IndexedModel();

        for(int i = 0; i < positions.size(); i++)
        {
            result.getPositions().add(positions.get(i));
            result.getTexCoords().add(new Vector2f());
            result.getNormals().add(new Vector3f());
            result.getTangents().add(new Vector3f());
            result.getColors().add(colors.get(i));
        }

        for(Integer index: indices)
        {
            result.getIndices().add(index);
        }

        result.calcNormals();
        result.calcTangents();

        return result;
    }
}
